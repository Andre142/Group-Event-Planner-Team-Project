package csci310.servlets;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoDatabase;
import csci310.models.Response;
import csci310.models.User;
import csci310.utilities.DatabaseManager;
import csci310.utilities.JsonHelper;
import csci310.utilities.K;
import csci310.utilities.UserDatabaseUtil;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
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

    @Before
    public void setUp() {
        user = new User("ExistingName","ExistingPsw");
        new K();
        mongoClient = new MongoClient(new MongoClientURI(K.mongoClientURI));
        mongoDatabase = mongoClient.getDatabase(K.dbName);
        mongoDatabase.drop();
        user.setUuid(UUID.randomUUID().toString());
        UserDatabaseUtil.insertUser(user);
        servlet = new LoginServlet();
    }

    @Test
    public void testdoGet_loginUnsuccessful() throws IOException {
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        when(request.getParameter("username")).thenReturn("NonExistingName");
        when(request.getParameter("psw")).thenReturn(("NonExistingPsw"));
        StringWriter stringWriter = new StringWriter();
        PrintWriter writer = new PrintWriter(stringWriter);
        when(response.getWriter()).thenReturn(writer);
        servlet.doGet(request,response);
        writer.flush();
        Response res = JsonHelper.shared().fromJson(stringWriter.toString(), Response.class);
        assertEquals("Either username or password is wrong.",res.getMessage());
        assertFalse(res.getStatus());
    }

    @Test
    public void testdoGet_loginSuccessful() throws IOException {
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        when(request.getParameter("username")).thenReturn("ExistingName");
        when(request.getParameter("psw")).thenReturn(("ExistingPsw"));
        StringWriter stringWriter = new StringWriter();
        PrintWriter writer = new PrintWriter(stringWriter);
        when(response.getWriter()).thenReturn(writer);
        servlet.doGet(request,response);
        writer.flush();
        String ResponseString = stringWriter.toString();
        Response res = JsonHelper.shared().fromJson(ResponseString, Response.class);
        assertEquals(null,res.getMessage());
        assertTrue(res.getStatus());
        assertTrue(ResponseString.contains("ExistingName"));
    }
}