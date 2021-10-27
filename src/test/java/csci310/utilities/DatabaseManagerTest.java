package csci310.utilities;

import csci310.models.User;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.*;

public class DatabaseManagerTest {

    private User user;

    @Before
    public void setUp() throws Exception {
        user = new User("user1","13579qwerty");
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
    }

    @Test
    public void testCheckUserExists_doesNotExist() {
        assertNull(DatabaseManager.shared().checkUserExists("false"));
    }

    @Test
    public void testInsertUser() {
        User user2 = new User("user2","123456");
        DatabaseManager.shared().insertUser(user2);
        User retrievedUser = DatabaseManager.shared().verifyUser(user2);
        assertEquals(retrievedUser.getUsername(),user2.getUsername());
    }

    @Test
    public void testVerifyUser_doesExist() {
        User user3 = new User("user3","123456");
        DatabaseManager.shared().insertUser(user3);
        User retrievedUser = DatabaseManager.shared().verifyUser(user3);
        assertEquals(retrievedUser.getUsername(),user3.getUsername());
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
        DatabaseManager.shared().insertUser(new User("user1","123"));
        DatabaseManager.shared().insertUser(new User("user2","123"));
        DatabaseManager.shared().insertUser(new User("3user","123"));
        ArrayList<String> arr = DatabaseManager.shared().searchUsers("user");
        assertNotNull(arr);
        assertNotEquals(0,arr.size());
    }

    @Test
    public void testSearchUsers_fail() {
        ArrayList<String> arr = DatabaseManager.shared().searchUsers("somerandomnonexistentuser");
        assertNull(arr);
    }
}