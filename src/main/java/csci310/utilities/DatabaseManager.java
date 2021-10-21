package csci310.utilities;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import static com.mongodb.client.model.Filters.*;
import csci310.models.User;
import org.bson.Document;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.sql.*;

public class DatabaseManager {

    private static DatabaseManager databaseManager;
    private Connection con;

    private PreparedStatement checkUserExistsPs;
    private PreparedStatement insertUserPs;
    private PreparedStatement verifyUserPs;

    // for unit tests purpose
    public static void setDatabaseManager(DatabaseManager databaseManager) {DatabaseManager.databaseManager = databaseManager;}
    public void setCheckUserExistsPs(PreparedStatement checkUserExistsPs) {this.checkUserExistsPs = checkUserExistsPs;}

    private DatabaseManager() {
        try {
            con = DriverManager.getConnection(K.sqliteUrl);

            createUsersTableIfNotExists();
            checkUserExistsPs = con.prepareStatement("select Salt from Users where Username = ?");
            insertUserPs = con.prepareStatement("insert into Users (Username,Salt,Hash) values (?,?,?)");
            verifyUserPs = con.prepareStatement("select Username from Users where Username = ? and Hash = ?");
        } catch (SQLException e) {}
    }

    private void createUsersTableIfNotExists() throws SQLException {
        String tableSql = "CREATE TABLE IF NOT EXISTS Users (" +
                "Username TEXT PRIMARY KEY," +
                "Salt TEXT NOT NULL," +
                "Hash TEXT NOT NULL" +
                ")";
        con.createStatement().execute(tableSql);
    }

    public static DatabaseManager shared() {
        if (databaseManager == null) {
            databaseManager = new DatabaseManager();
        }
        return databaseManager;
    }

    public String checkUserExists(String username) {
        try {
            checkUserExistsPs.setString(1,username);
            ResultSet rs = checkUserExistsPs.executeQuery();
            if (rs.next())
                return rs.getString(1);
        } catch (SQLException e) {}
        return null;
    }

    public void insertUser(User user) {
        String salt = SecurePasswordHelper.getSalt();
        String hash = SecurePasswordHelper.getSHA512SecurePassword(user.getPsw(),salt,"SHA-512");
        try {
            insertUserPs.setString(1,user.getUsername());
            insertUserPs.setString(2,salt);
            insertUserPs.setString(3,hash);
            insertUserPs.executeUpdate();
        } catch (SQLException e) {}
    }

    public User verifyUser(User user) {
        try {
            checkUserExistsPs.setString(1,user.getUsername());
            ResultSet rs = checkUserExistsPs.executeQuery();
            if (rs.next()) {
                verifyUserPs.setString(1,user.getUsername());
                String salt = rs.getString(1);
                String hash = SecurePasswordHelper.getSHA512SecurePassword(user.getPsw(),salt,"SHA-512");
                verifyUserPs.setString(2,hash);
                ResultSet rs2 = verifyUserPs.executeQuery();
                if (rs2.next())
                    return user;
            }
        } catch (SQLException e) {}
        return null;
    }
}
