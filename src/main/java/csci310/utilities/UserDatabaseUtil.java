package csci310.utilities;

import org.bson.Document;
import csci310.models.User;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;


public class UserDatabaseUtil {
    public static String get_SHA_512_SecurePassword(String passwordToHash, String salt) {
        String generatedPassword = null;
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-512");
            md.update(salt.getBytes(StandardCharsets.UTF_8));
            byte[] bytes = md.digest(passwordToHash.getBytes(StandardCharsets.UTF_8));
            StringBuilder sb = new StringBuilder();
            for(int i=0; i< bytes.length ;i++){
                sb.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
            }
            generatedPassword = sb.toString();
        } catch (NoSuchAlgorithmException e) {}
        return generatedPassword;
    }
    public static String verifyUser(User user) {
        Document document = DatabaseManager.shared().findInCollection("user", "username", user.getUsername());
        if(document != null) {
            String salt = document.getString("salt");
            String hash = get_SHA_512_SecurePassword(user.getPsw(),salt);
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
        String hash = get_SHA_512_SecurePassword(user.getPsw(),salt);
        Document newUser = new Document().
                append("username",user.getUsername()).
                append("uuid", user.getUuid()).
                append("hash",hash).
                append("salt",salt);
        DatabaseManager.shared().insertInCollection("user", newUser);
    }
}
