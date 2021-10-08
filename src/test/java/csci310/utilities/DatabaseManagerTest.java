package csci310.utilities;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoDatabase;
import csci310.models.User;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.UUID;

import static org.junit.Assert.*;

public class DatabaseManagerTest {

    MongoClient mongoClient;
    MongoDatabase mongoDatabase;

    User user;

    @Before
    public void setUp() {
        user = new User("user1","13579qwerty");

        new K();
        mongoClient = new MongoClient(new MongoClientURI(K.mongoClientURI));
        mongoDatabase = mongoClient.getDatabase(K.dbName);
        mongoDatabase.drop();
    }

    @Test
    public void testshared() {
        assertTrue(DatabaseManager.shared() != null);
    }

    @Test
    public void testverifyUser_doesExist() {
        try {
            DatabaseManager.shared().insertUser(user);
        } catch (NoSuchAlgorithmException e) {}
        String userJson = DatabaseManager.shared().verifyUser(user);
        User user1 = JsonHelper.shared().fromJson(userJson, User.class);
        // user is inserted into db
        assertEquals(user.getUsername(),user1.getUsername());
    }

    @Test
    public void testverifyUser_doesNotExist() {
        User nonExistentUser = new User(UUID.randomUUID().toString(),"12345678");
        String userJson = DatabaseManager.shared().verifyUser(nonExistentUser);
        assertTrue(userJson == null);
    }

    @Test
    public void testverifyUser_incorrectPsw() {
        user.setUsername("newName");
        user.setPsw("abcdefgh");
        user.setUuid(UUID.randomUUID().toString());
        try {
            DatabaseManager.shared().insertUser(user);
        } catch (NoSuchAlgorithmException e) {}
        User incorrectPswUser = new User(user.getUsername(), "incorrect");
        String userJson = DatabaseManager.shared().verifyUser(incorrectPswUser);
        assertTrue(userJson == null);
    }

    @Test
    public void testcheckUserExists_doesExist() {
        try {
            DatabaseManager.shared().insertUser(user);
        } catch (NoSuchAlgorithmException e) {}
        assertTrue(DatabaseManager.shared().checkUserExists(user.getUsername()));
    }

    @Test
    public void testcheckUserExists_doesNotExist() {
        User randomUser = new User(UUID.randomUUID().toString(),"12345678");
        assertTrue(!DatabaseManager.shared().checkUserExists(randomUser.getUsername()));
    }

    @Test
    public void testinsertUser() {
        try {
            DatabaseManager.shared().insertUser(user);
        } catch (NoSuchAlgorithmException e) {}
        String userJson = DatabaseManager.shared().verifyUser(user);
        User user1 = JsonHelper.shared().fromJson(userJson, User.class);
        // user is inserted into db
        assertEquals(user.getUsername(),user1.getUsername());
    }

    @After
    public void tearDown() {
        mongoDatabase.drop();
        mongoClient.close();
    }
}