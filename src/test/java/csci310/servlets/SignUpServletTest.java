package csci310.servlets;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoDatabase;
import csci310.models.User;
import csci310.utilities.DatabaseManager;
import csci310.utilities.K;
import org.apache.struts.mock.MockHttpServletRequest;
import org.apache.struts.mock.MockHttpServletResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.UUID;

import static org.junit.Assert.*;

public class SignUpServletTest {

    private SignUpServlet servlet;
    private User user;
    private MongoClient mongoClient;
    private MongoDatabase mongoDatabase;
    private MockHttpServletRequest request;
    private MockHttpServletResponse response;

    @Before
    public void setUp() {
        user = new User("user1","13579qwerty");
        new K();
        mongoClient = new MongoClient(new MongoClientURI(K.mongoClientURI));
        mongoDatabase = mongoClient.getDatabase(K.dbName);
        mongoDatabase.drop();
        user.setUsername("ExistingName");
        user.setPsw("ExistingPassword");
        user.setUuid(UUID.randomUUID().toString());
        try {
            DatabaseManager.shared().insertUser(user);
        } catch (NoSuchAlgorithmException e) {}
        request = new MockHttpServletRequest();
        response = new MockHttpServletResponse();
        servlet = new SignUpServlet();
    }

    @Test
    public void testdoPost_SignupUnsuccessful() throws IOException {
        request.addParameter("username", "ExistingName");
        request.addParameter("password", "random");
        servlet.doPost(request, response);
        assertEquals("application/json", response.getContentType());
        String wrong = "The username has been associated with an account.";
        assertTrue(response.getWriter().toString().contains(wrong));
    }
}