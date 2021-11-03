package csci310.utilities;

import csci310.models.Event;
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

    private PreparedStatement insertSentProposalPs;
    private PreparedStatement insertReceivedProposalPs;
    private PreparedStatement insertEventPs;

    private PreparedStatement getSentProposalIDsPs;
    private PreparedStatement getReceivedProposalIDsPs;
    private PreparedStatement getProposalTitlePs;
    private PreparedStatement getSenderUsernameInProposalPs;
    private PreparedStatement getReceiverUsernamesInProposalPs;
    private PreparedStatement getEventsInProposalPs;

    private DatabaseManager() {
        try {
            con = DriverManager.getConnection(K.sqliteUrl);

            createTablesIfNotExist();
            checkUserExistsPs = con.prepareStatement("select Salt from Users where Username = ?");
            insertUserPs = con.prepareStatement("insert into Users (Username,Salt,Hash) values (?,?,?)");
            verifyUserPs = con.prepareStatement("select Username from Users where Username = ? and Hash = ?");
            deleteUserPs = con.prepareStatement("delete from Users where Username = ?");

            searchUsersPs = con.prepareStatement("select Username from Users where Username like ?");

            insertSentProposalPs = con.prepareStatement("insert into SentProposals (ProposalID, ProposalTitle, SenderUsername) values (?,?,?)");
            insertReceivedProposalPs = con.prepareStatement("insert into ReceivedProposals (ReceiverUsername, ProposalID) values (?,?)");
            insertEventPs = con.prepareStatement("insert into Events (EventID, EventName, EventDate, EventTime, EventUrl, EventGenre, ProposalID) values (?,?,?,?,?,?,?)");

            getSentProposalIDsPs = con.prepareStatement("select ProposalID from SentProposals where SenderUsername = ?");
            getReceivedProposalIDsPs = con.prepareStatement("select ProposalID from ReceivedProposals where ReceiverUsername = ?");
            getProposalTitlePs = con.prepareStatement("select ProposalTitle from SentProposals where ProposalID = ?");
            getSenderUsernameInProposalPs = con.prepareStatement("select SenderUsername from SentProposals where ProposalID = ?");
            getReceiverUsernamesInProposalPs = con.prepareStatement("select ReceiverUsername from ReceivedProposals where ProposalID = ?");
            getEventsInProposalPs = con.prepareStatement("select EventID, EventName, EventDate, EventTime, EventUrl, EventGenre from Events where ProposalID = ?");

            throw new SQLException();
        } catch (SQLException e) {}
    }

    private void createTablesIfNotExist() throws SQLException {
        Statement st = con.createStatement();
        st.execute("create table if not exists Users (" +
                "Username text primary key," +
                "Salt text not null," +
                "Hash text not null" +
                ");");
        st.execute("create table if not exists SentProposals (" +
                "ProposalID text primary key," +
                "ProposalTitle text not null," +
                "SenderUsername text not null" +
                ");");
        st.execute("create table if not exists ReceivedProposals (" +        // each receiver has a copy of the proposal
                "ReceiverUsername text not null," +
                "ProposalID text not null," +
                "foreign key(ProposalID) references SentProposals(ProposalID)," +
                "primary key(ReceiverUsername, ProposalID)" +
                ");");
        st.execute("create table if not exists Events (" +               // a list of event contained in a proposal
                "EventID text primary key," +
                "EventName text not null," +
                "EventDate text not null," +
                "EventTime text not null," +
                "EventUrl text not null," +
                "EventGenre text not null," +
                "ProposalID text not null," +
                "foreign key(ProposalID) references SentProposals(ProposalID)" +
                ");");
        st.execute("create table if not exists RatedEvents (" +        // each receiver has a copy of the event to rate, only insert/update when someone posts/modifies rating
                "ReceiverUsername text not null," +
                "Excitement integer not null," +                        // 1-5 scale
                "Availability integer not null," +
                "EventID text not null," +                      // 1 for yes; 0 for no
                "foreign key(EventID) references Events(EventID)," +
                "primary key(ReceiverUsername, EventID)" +
                ");");
    }

    public static DatabaseManager shared() {
        if (databaseManager == null) {
            databaseManager = new DatabaseManager();
        }
        return databaseManager;
    }

    // Authentication

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
            System.out.println("salt - " + salt);
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
            while (rs.next())
                users.add(rs.getString(1));
            if (!users.isEmpty())
                return users;
            throw new SQLException();
        } catch (SQLException e) {}
        return null;
    }

    // Send Proposal

    public void insertSentProposal(String proposalID, String proposalTitle, String senderUsername) {
        try {
            insertSentProposalPs.setString(1,proposalID);
            insertSentProposalPs.setString(2,proposalTitle);
            insertSentProposalPs.setString(3,senderUsername);
            insertSentProposalPs.executeUpdate();
        } catch (SQLException e) {}
    }

    public void insertReceivedProposal(String receiverUsername, String proposalID) {
        try {
            insertReceivedProposalPs.setString(1,receiverUsername);
            insertReceivedProposalPs.setString(2,proposalID);
            insertReceivedProposalPs.executeUpdate();
        } catch (SQLException e) {}
    }

    public void insertEvent(Event event, String proposalID) {
        try {
            insertEventPs.setString(1,event.getEventID());
            insertEventPs.setString(2,event.getName());
            insertEventPs.setString(3,event.getDate());
            insertEventPs.setString(4,event.getTime());
            insertEventPs.setString(5,event.getUrl());
            insertEventPs.setString(6,event.getGenre());
            insertEventPs.setString(7,proposalID);
            insertEventPs.executeUpdate();
        } catch (SQLException e) {}
    }

    // Get Sent and Received Proposal

    public ArrayList<String> getProposalIDs(String username, Boolean isSent) {
        ArrayList<String> proposalIDs = new ArrayList<>();
        try {
            ResultSet rs;
            if (isSent) {
                getSentProposalIDsPs.setString(1, username);
                rs = getSentProposalIDsPs.executeQuery();
            } else {
                getReceivedProposalIDsPs.setString(1, username);
                rs = getReceivedProposalIDsPs.executeQuery();
            }
            while (rs.next())
                proposalIDs.add(rs.getString(1));
            throw new SQLException();
        } catch (SQLException e) {}
        return proposalIDs;
    }

    public String getProposalTitle(String proposalID) {
        String proposalTitle = null;
        try {
            getProposalTitlePs.setString(1,proposalID);
            ResultSet rs = getProposalTitlePs.executeQuery();
            proposalTitle = rs.getString(1);
            throw new SQLException();
        } catch (SQLException e) {}
        return proposalTitle;
    }

    public String getSenderUsernameInProposal(String proposalID) {
        String senderUsername = null;
        try {
            getSenderUsernameInProposalPs.setString(1, proposalID);
            ResultSet rs = getSenderUsernameInProposalPs.executeQuery();
            senderUsername = rs.getString(1);
            throw new SQLException();
        } catch (SQLException e) {}
        return senderUsername;
    }

    public ArrayList<String> getReceiverUsernamesInProposal(String proposalID) {
        ArrayList<String> usernames = new ArrayList<>();
        try {
            getReceiverUsernamesInProposalPs.setString(1,proposalID);
            ResultSet rs = getReceiverUsernamesInProposalPs.executeQuery();
            while (rs.next())
                usernames.add(rs.getString(1));
            throw new SQLException();
        } catch (SQLException e) {}
        return usernames;
    }

    public ArrayList<Event> getEventsInProposal(String proposalID) {
        ArrayList<Event> events = new ArrayList<>();
        try {
            getEventsInProposalPs.setString(1,proposalID);
            ResultSet rs = getEventsInProposalPs.executeQuery();
            while (rs.next()) {
                String id = rs.getString(1);
                String name = rs.getString(2);
                String date = rs.getString(3);
                String time = rs.getString(4);
                String url = rs.getString(5);
                String genre = rs.getString(6);
                events.add(new Event(name,date,time,url,genre,id));
            }
            throw new SQLException();
        } catch (SQLException e) {}
        return events;
    }

}
