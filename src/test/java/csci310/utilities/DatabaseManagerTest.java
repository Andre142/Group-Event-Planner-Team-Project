package csci310.utilities;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoDatabase;
import csci310.models.User;
import org.bson.Document;
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
    public void testFindInCollection() {
        UserDatabaseUtil.insertUser(user);
        String userJson = null;
        Document document = DatabaseManager.shared().findInCollection("user", "username", user.getUsername());
        if(document != null) {
            String salt = document.getString("salt");
            String hash = UserDatabaseUtil.get_SHA_512_SecurePassword(user.getPsw(),salt);
            if(hash.equals(document.getString("hash"))){
//               password is correct
                userJson = document.toJson();
            }
        }
        User user1 = JsonHelper.shared().fromJson(userJson, User.class);
        // user is inserted into db
        assertEquals(user.getUsername(),user1.getUsername());
    }

    @Test
    public void insertInCollection() {
        //      generate salt
        SecureRandom random = new SecureRandom();
        byte[] bytes = new byte[16];
        random.nextBytes(bytes);
        String salt = bytes.toString();
//      hashing
        String hash = UserDatabaseUtil.get_SHA_512_SecurePassword(user.getPsw(),salt);
        Document newUser = new Document().
                append("username",user.getUsername()).
                append("uuid", user.getUuid()).
                append("hash",hash).
                append("salt",salt);
        DatabaseManager.shared().insertInCollection("user", newUser);
        String userJson = UserDatabaseUtil.verifyUser(user);
        User user1 = JsonHelper.shared().fromJson(userJson, User.class);
        // user is inserted into db
        assertEquals(user.getUsername(),user1.getUsername());

    }
}