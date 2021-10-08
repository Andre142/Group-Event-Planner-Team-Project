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

public class DatabaseManager {

    private static DatabaseManager databaseManager;

    private MongoClient mongoClient;
    private MongoDatabase db;

    private DatabaseManager() {
        mongoClient = new MongoClient(new MongoClientURI(K.mongoClientURI));
        db = mongoClient.getDatabase(K.dbName);
    }

    public static DatabaseManager shared() {
        if (databaseManager == null) {
            databaseManager = new DatabaseManager();
        }
        return databaseManager;
    }

    public String verifyUser(User user) {
        MongoCollection<Document> collection = db.getCollection("user");
        Document document = collection.find(eq("username",user.getUsername())).first();
//      user exists
        if (document != null){
            String salt = document.getString("salt");
            String hash = SecurePasswordHelper.getSHA512SecurePassword(user.getPsw(),salt);
            if(hash.equals(document.getString("hash"))){
//               password is correct
                return document.toJson();
            }
        }
        return null;
    }

    public Boolean checkUserExists(String username) {
        MongoCollection<Document> collection = db.getCollection("user");
        Document document = collection.find(eq("username",username)).first();
        return document != null;
    }

    public void insertUser(User user) throws NoSuchAlgorithmException {
//      generate salt
        SecureRandom random = new SecureRandom();
        byte[] bytes = new byte[16];
        random.nextBytes(bytes);
        String salt = bytes.toString();
//      hashing
        String hash = SecurePasswordHelper.getSHA512SecurePassword(user.getPsw(),salt);
        MongoCollection collection = db.getCollection("user");
        Document newUser = new Document().
                append("username",user.getUsername()).
                append("uuid", user.getUuid()).
                append("hash",hash).
                append("salt",salt);
        collection.insertOne(newUser);
    }

}
