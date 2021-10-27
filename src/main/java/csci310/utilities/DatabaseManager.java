package csci310.utilities;

import csci310.models.User;
import java.sql.*;
import java.util.ArrayList;

public class DatabaseManager {

    private static DatabaseManager databaseManager;
    private Connection con;

    private PreparedStatement checkUserExistsPs;
    private PreparedStatement insertUserPs;
    private PreparedStatement verifyUserPs;
    private PreparedStatement deleteUserPs;

    private PreparedStatement searchUsersPs;

    private DatabaseManager() {
        try {
            con = DriverManager.getConnection(K.sqliteUrl);

            createTablesIfNotExist();
            checkUserExistsPs = con.prepareStatement("select Salt from Users where Username = ?");
            insertUserPs = con.prepareStatement("insert into Users (Username,Salt,Hash) values (?,?,?)");
            verifyUserPs = con.prepareStatement("select Username from Users where Username = ? and Hash = ?");
            deleteUserPs = con.prepareStatement("delete from Users where Username = ?");

            searchUsersPs = con.prepareStatement("select Username from Users where Username like ?");

            throw new SQLException();
        } catch (SQLException e) {}
    }

    private void createTablesIfNotExist() throws SQLException {
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

    // authentication

    public String checkUserExists(String username) {
        try {
            checkUserExistsPs.setString(1,username);
            ResultSet rs = checkUserExistsPs.executeQuery();
            if (rs.next())
                return rs.getString(1);
            throw new SQLException();
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
            throw new SQLException();
        } catch (SQLException e) {}
        return null;
    }

    public void deleteUser(User user) {
        try {
            if (checkUserExists(user.getUsername()) != null) {
                deleteUserPs.setString(1,user.getUsername());
                deleteUserPs.executeUpdate();
            } else {
                throw new SQLException();
            }
        } catch (SQLException e) {}
    }

    // Search User

    public ArrayList<String> searchUsers(String usernameSubstring) {
        ArrayList<String> users = new ArrayList<>();
        try {
            searchUsersPs.setString(1,"%"+usernameSubstring+"%");
            ResultSet rs = searchUsersPs.executeQuery();
            while (rs.next()) {
                users.add(rs.getString(1));
            }
            if (!users.isEmpty())
                return users;
            throw new SQLException();
        } catch (SQLException e) {}
        return null;
    }
}
