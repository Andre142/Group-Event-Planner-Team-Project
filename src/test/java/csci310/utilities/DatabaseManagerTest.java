package csci310.utilities;

import csci310.models.User;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.sql.PreparedStatement;
import java.sql.SQLException;

import static org.junit.Assert.*;
import static org.mockito.Mockito.when;

public class DatabaseManagerTest {

    private User user;

    @Mock
    PreparedStatement preparedStatementMock;

    @Before
    public void setUp() throws Exception {
        user = new User("user1","13579qwerty");
        new K();
        MockitoAnnotations.initMocks(this);
        when(preparedStatementMock.executeQuery()).thenThrow(new SQLException());
    }

    // clean up after using Mock statement
    private void cleanUp() {
        DatabaseManager.setDatabaseManager(null);
    }

    @Test
    public void testShared() {
        assertNotNull(DatabaseManager.shared());
     }

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
    public void testCheckUserExists_throwsException() {
        DatabaseManager.shared().setCheckUserExistsPs(preparedStatementMock);
        DatabaseManager.shared().checkUserExists("false");
        cleanUp();
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
        DatabaseManager.shared().verifyUser(newUser);
    }

    @Test
    public void testVerifyUser_throwsException() {
        DatabaseManager.shared().setCheckUserExistsPs(preparedStatementMock);
        User user4 = new User("user4","123456");
        DatabaseManager.shared().verifyUser(user4);
        K.sqliteUrl = "";
        cleanUp();
        DatabaseManager.shared();
        K.sqliteUrl = "jdbc:sqlite:groupie.db";
        cleanUp();
        assertNull(DatabaseManager.shared().verifyUser(user4));
    }

}