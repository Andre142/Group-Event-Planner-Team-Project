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
import org.mockito.Mockito;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.security.NoSuchAlgorithmException;
import java.util.UUID;

import static org.junit.Assert.*;

public class LoginServletTest extends Mockito{
    private LoginServlet servlet;
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
        servlet = new LoginServlet();
    }

    @Test
    public void testdoGet_loginUnsuccessful() throws IOException {
        request.addParameter("username", "NonExistingName");
        request.addParameter("password", "NonExistingPassword");
        servlet.doGet(request, response);
        assertEquals("application/json", response.getContentType());
        String wrong = "Either username or password is wrong.";
        assertTrue(response.getWriter().toString().contains(wrong));
//        assertFalse(response.getWriter()["status"]);
    }

//    @Test
//    public void testdoGet_loginSuccessful() throws IOException {
//
//    }
}