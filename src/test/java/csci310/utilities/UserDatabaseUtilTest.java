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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertNotNull;

public class UserDatabaseUtilTest {
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
    public void testInit() {
        assertNotNull(new UserDatabaseUtil());
    }

    @Test
    public void testverifyUser_doesExist() {
        UserDatabaseUtil.insertUser(user);
        String userJson = UserDatabaseUtil.verifyUser(user);
        User user1 = JsonHelper.shared().fromJson(userJson, User.class);
        // user is inserted into db
        assertEquals(user.getUsername(),user1.getUsername());
    }

    @Test
    public void testverifyUser_doesNotExist() {
        User nonExistentUser = new User(UUID.randomUUID().toString(),"12345678");
        String userJson = UserDatabaseUtil.verifyUser(nonExistentUser);
        assertTrue(userJson == null);
    }

    @Test
    public void testverifyUser_incorrectPsw() {
        user.setUsername("newName");
        user.setPsw("abcdefgh");
        user.setUuid(UUID.randomUUID().toString());
        UserDatabaseUtil.insertUser(user);
        User incorrectPswUser = new User(user.getUsername(), "incorrect");
        String userJson = UserDatabaseUtil.verifyUser(incorrectPswUser);
        assertTrue(userJson == null);
    }

    @Test
    public void testcheckUserExists_doesExist() {
        UserDatabaseUtil.insertUser(user);
        assertTrue(UserDatabaseUtil.checkUserExists(user.getUsername()));
    }

    @Test
    public void testcheckUserExists_doesNotExist() {
        User randomUser = new User(UUID.randomUUID().toString(),"12345678");
        assertTrue(!UserDatabaseUtil.checkUserExists(randomUser.getUsername()));
    }

    @Test
    public void testinsertUser() {
        UserDatabaseUtil.insertUser(user);
        String userJson = UserDatabaseUtil.verifyUser(user);
        User user1 = JsonHelper.shared().fromJson(userJson, User.class);
        // user is inserted into db
        assertEquals(user.getUsername(),user1.getUsername());
    }

    @Test
    public void testget_SHA_512_SecurePassword() {
        String psw = "abcdefgh";
        SecureRandom random = new SecureRandom();
        byte[] bytes = new byte[16];
        random.nextBytes(bytes);
        String salt = bytes.toString();
        String hashedPsw = UserDatabaseUtil.get_SHA_512_SecurePassword(psw,salt);
        assertTrue(hashedPsw.length() > 50);
        assertTrue(hashedPsw != psw);
    }

    @After
    public void tearDown() {
        mongoDatabase.drop();
        mongoClient.close();
    }
}