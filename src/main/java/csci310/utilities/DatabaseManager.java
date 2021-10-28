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
        String tableSql = "create table if not exists Users (" +
                    "Username text primary key," +
                    "Salt text not null," +
                    "Hash text not null" +
                ");" +
                "create table if not exists SentProposals (" +
                    "ProposalID text primary key," +
                    "SenderUsername text not null" +
                ");" +
                "create table if not exists ReceivedProposals (" +      // each receiver has a copy of the proposal
                    "ReceiverUsername text not null," +
                    "ProposalID text foreign key references SentProposals(ProposalID)," +
                    "primary key(ReceiverUsername, ProposalID)" +
                ");" +
                "create table if not exists Events (" +     // a list of event contained in a proposal
                    "EventID text primary key," +
                    "EventName text not null," +
                    "EventDate text not null," +
                    "EventUrl text not null," +
                    "ProposalID text foreign key references SentProposals(ProposalID)" +
                ");" +
                "create table if not exists RatedEvents (" +        // each receiver has a copy of the event to rate, only insert/update when someone posts/modifies rating
                    "ReceiverUsername text not null," +
                    "Excitement integer not null," +            // 1-5 scale
                    "Availability integer not null," +      // 1 for yes; 0 for no
                    "EventID text foreign key references Events(EventID)," +
                    "primary key(ReceiverUsername, EventID)" +
                ");";
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
