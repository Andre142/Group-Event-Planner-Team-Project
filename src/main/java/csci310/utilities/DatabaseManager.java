package csci310.utilities;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import static com.mongodb.client.model.Filters.*;
import csci310.models.User;
import org.bson.Document;

public class DatabaseManager {

    private static DatabaseManager databaseManager;

    private MongoClient mongoClient;
    private MongoDatabase db;

    private DatabaseManager() {
        mongoClient = new MongoClient(new MongoClientURI("mongodb://mongo:27017"));
        db = mongoClient.getDatabase("groupie-team-31");
    }

    public static DatabaseManager shared() {
        if (databaseManager == null) {
            databaseManager = new DatabaseManager();
        }
        return databaseManager;
    }

    public String verifyUser(User user) {
        MongoCollection<Document> collection = db.getCollection("user");
        Document document = collection.find(and(eq("username",user.getUsername()), eq("psw",user.getPsw()))).first();
        return document == null ? null : document.toJson();
    }

    public Boolean checkUserExists(String username) {
        MongoCollection<Document> collection = db.getCollection("user");
        Document document = collection.find(eq("username",username)).first();
        return document != null;
    }

    public void insertUser(User user) {
        MongoCollection collection = db.getCollection("user");
        collection.insertOne(user.toDocument());
    }
}
