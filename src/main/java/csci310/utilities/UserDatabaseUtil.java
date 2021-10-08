package csci310.utilities;

import org.bson.Document;
import csci310.models.User;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;


public class UserDatabaseUtil {

    public static String verifyUser(User user) {
        Document document = DatabaseManager.shared().findInCollection("user", "username", user.getUsername());
        if(document != null) {
            String salt = document.getString("salt");
            String hash = SecurePasswordHelper.getSHA512SecurePassword(user.getPsw(),salt);
            if(hash.equals(document.getString("hash"))){
//               password is correct
                return document.toJson();
            }
        }
        return null;
    }
    public static Boolean checkUserExists(String username) {
       return DatabaseManager.shared().findInCollection("user", "username", username) != null;
    }

    public static void insertUser(User user) {
        //      generate salt
        SecureRandom random = new SecureRandom();
        byte[] bytes = new byte[16];
        random.nextBytes(bytes);
        String salt = bytes.toString();
//      hashing
        String hash = SecurePasswordHelper.getSHA512SecurePassword(user.getPsw(),salt);
        Document newUser = new Document().
                append("username",user.getUsername()).
                append("uuid", user.getUuid()).
                append("hash",hash).
                append("salt",salt);
        DatabaseManager.shared().insertInCollection("user", newUser);
    }
}
