package csci310.models;

import org.junit.Test;
import org.bson.Document;

import java.util.UUID;

import static org.junit.Assert.*;

public class UserTest {

    @Test
    public void testtoDocument() {
        String uuid = UUID.randomUUID().toString();
        User user = new User("name1","12345678",uuid);
        Document userDocument = user.toDocument();
        assertEquals("name1",userDocument.get("username"));
        assertEquals("12345678",userDocument.get("psw"));
        assertEquals(uuid,userDocument.get("uuid"));
    }
}