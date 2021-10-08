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

    public Document findInCollection(String collectionName, String field, String value) {
        MongoCollection<Document> collection = db.getCollection(collectionName);
        Document document = collection.find(eq(field,value)).first();
        return document;
    }

    public void insertInCollection(String collectionName, Document document) {
        MongoCollection collection = db.getCollection("user");
        collection.insertOne(document);
    }
}
