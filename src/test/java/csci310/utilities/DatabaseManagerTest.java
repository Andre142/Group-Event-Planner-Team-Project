package csci310.utilities;

import csci310.models.Event;
import csci310.models.User;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.*;

public class DatabaseManagerTest {

    private User user;
    private String id;
    private String title;
    private String senderUsername;
    private String receiverUsername;

    @Before
    public void setUp() throws Exception {
        user = new User("user1","13579qwerty");
        id = "123456qwerty";
        title = "title";
        senderUsername = "senderJunitTester";
        receiverUsername = "receiverJunitTester";
        new K();
    }

    @Test
    public void testShared() {
        assertNotNull(DatabaseManager.shared());
    }

    // authentication

    @Test
    public void testCheckUserExists_doesExist() {
        DatabaseManager.shared().insertUser(user);
        assertNotNull(DatabaseManager.shared().checkUserExists(user.getUsername()));
        DatabaseManager.shared().deleteUser(user);
    }

    @Test
    public void testCheckUserExists_doesNotExist() {
        assertNull(DatabaseManager.shared().checkUserExists("false"));
    }

    @Test
    public void testInsertUser() {
        User user2 = new User("user2","123");
        DatabaseManager.shared().insertUser(user2);
        User retrievedUser = DatabaseManager.shared().verifyUser(user2);
        assertEquals(retrievedUser.getUsername(),user2.getUsername());
        DatabaseManager.shared().deleteUser(user2);
    }

    @Test
    public void testVerifyUser_doesExist() {
        User user3 = new User("user3","123456");
        DatabaseManager.shared().insertUser(user3);
        User retrievedUser = DatabaseManager.shared().verifyUser(user3);
        assertEquals(retrievedUser.getUsername(),user3.getUsername());
        DatabaseManager.shared().deleteUser(user3);
    }

    @Test
    public void testVerifyUser_doesNotExist() {
        User user4 = new User("user4","123456");
        assertNull(DatabaseManager.shared().verifyUser(user4));
    }

    @Test
    public void testVerifyUser_incorrectPsw() {
        DatabaseManager.shared().insertUser(user);
        User newUser = new User(user.getUsername(),"false");
        assertNull(DatabaseManager.shared().verifyUser(newUser));
        DatabaseManager.shared().deleteUser(user);
    }

    @Test
    public void testDeleteUser_doesExist() {
        DatabaseManager.shared().insertUser(user);
        DatabaseManager.shared().deleteUser(user);
        assertNull(DatabaseManager.shared().checkUserExists(user.getUsername()));
    }

    @Test
    public void testDeleteUser_doesNotExist() {
        User aUser = new User("aUser","123456");
        DatabaseManager.shared().deleteUser(aUser);
        assertNull(DatabaseManager.shared().checkUserExists(aUser.getUsername()));
    }

    // search user

    @Test
    public void testSearchUsers_success() {
        User user1 = new User("user1","123");
        User user2 = new User("user2","123");
        User user3 = new User("3user","123");
        DatabaseManager.shared().insertUser(user1);
        DatabaseManager.shared().insertUser(user2);
        DatabaseManager.shared().insertUser(user3);
        ArrayList<String> arr = DatabaseManager.shared().searchUsers("user");
        assertNotNull(arr);
        assertNotEquals(0,arr.size());
        DatabaseManager.shared().deleteUser(user1);
        DatabaseManager.shared().deleteUser(user2);
        DatabaseManager.shared().deleteUser(user3);
    }

    @Test
    public void testSearchUsers_fail() {
        ArrayList<String> arr = DatabaseManager.shared().searchUsers("somerandomnonexistentuser");
        assertNull(arr);
    }

    // Send Proposal

    @Test
    public void testInsertSentProposal() {
        DatabaseManager.shared().insertSentProposal("idproposal23",title,senderUsername);
        assertTrue(DatabaseManager.shared().getProposalIDs(senderUsername,true).contains("idproposal23"));
    }

    @Test
    public void testInsertReceivedProposal() {
        DatabaseManager.shared().insertReceivedProposal(receiverUsername,"id233");
        assertTrue(DatabaseManager.shared().getProposalIDs(receiverUsername,false).contains("id233"));
    }

    @Test
    public void testInsertEvent() {
        Event event = new Event("event 1","2021-01-01","19:00:00","abc.com","music","eventiddew");
        DatabaseManager.shared().insertEvent(event,"eventproposalid12344");
        assertTrue(DatabaseManager.shared().getEventsInProposal("eventproposalid12344").size()>=1);
    }

    @Test
    public void testGetProposalIDs_sent() {
        DatabaseManager.shared().insertSentProposal(id,title,senderUsername);
        assertTrue(DatabaseManager.shared().getProposalIDs(senderUsername,true).contains(id));
    }

    @Test
    public void testGetProposalIDs_received() {
        DatabaseManager.shared().insertReceivedProposal(receiverUsername,id);
        assertTrue(DatabaseManager.shared().getProposalIDs(receiverUsername,false).contains(id));
    }

    @Test
    public void testGetProposalTitle() {
        DatabaseManager.shared().insertSentProposal("someidsrff",title,senderUsername);
        assertEquals(title,DatabaseManager.shared().getProposalTitle("someidsrff"));
    }

    @Test
    public void testGetSenderUsernameInProposal() {
        DatabaseManager.shared().insertSentProposal(id,title,senderUsername);
        assertEquals(senderUsername,DatabaseManager.shared().getSenderUsernameInProposal(id));
    }

    @Test
    public void testGetReceiverUsernamesInProposal() {
        DatabaseManager.shared().insertReceivedProposal(receiverUsername,id);
        assertTrue(DatabaseManager.shared().getReceiverUsernamesInProposal(id).contains(receiverUsername));
    }

    @Test
    public void testGetEventsInProposal() {
        Event event1= new Event("event 1","2021-01-01","19:00:00","abc.com","music","junitevent123456qwerty");
        Event event2 = new Event("event 2","2021-01-01","19:00:00","abc.com","music","junitevent123456");
        DatabaseManager.shared().insertEvent(event1,id);
        DatabaseManager.shared().insertEvent(event2,id);
        assertTrue(DatabaseManager.shared().getEventsInProposal(id).size()>=2);
    }
}