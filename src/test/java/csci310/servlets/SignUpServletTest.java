package csci310.servlets;

import csci310.models.Response;
import csci310.models.User;
import csci310.utilities.DatabaseManager;
import csci310.utilities.JsonHelper;
import csci310.utilities.K;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.UUID;

import static org.junit.Assert.*;

public class SignUpServletTest extends Mockito {
    private SignUpServlet servlet;
    private User user;

    @Before
    public void setUp() {
        user = new User("ExistingName","ExistingPsw");
        new K();
        user.setUuid(UUID.randomUUID().toString());
        DatabaseManager.shared().insertUser(user);
        servlet = new SignUpServlet();
    }

    @Test
    public void testdoPost_SignUpUnsuccessful() throws IOException {
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
//      create a mock sign_up user with an existing user name
        User SignUpUser = new User("ExistingName","RandomPsw");
//      create a buffer reader
        BufferedReader bufferedReader = new BufferedReader(new StringReader(JsonHelper.shared().toJson(SignUpUser)));
//      return the created buffer reader
        when(request.getReader()).thenReturn(bufferedReader);
//      create a string writer
        StringWriter stringWriter = new StringWriter();
        PrintWriter writer = new PrintWriter(stringWriter);
        when(response.getWriter()).thenReturn(writer);
        servlet.doPost(request,response);
        writer.flush();
        Response res = JsonHelper.shared().fromJson(stringWriter.toString(), Response.class);
        assertFalse(res.getStatus());
        assertEquals("The username has been associated with an account.",res.getMessage());
    }

    @Test
    public void testdoPost_SignUpSuccessful() throws IOException {
        String username = "NonExistingName";
        String psw = "RandomPsw";
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
//      create a mock sign_up user with an existing user name
        User SignUpUser = new User(username,psw);
//      create a buffer reader
        BufferedReader bufferedReader = new BufferedReader(new StringReader(JsonHelper.shared().toJson(SignUpUser)));
//      return the created buffer reader
        when(request.getReader()).thenReturn(bufferedReader);
//      create a string writer
        StringWriter stringWriter = new StringWriter();
        PrintWriter writer = new PrintWriter(stringWriter);
        when(response.getWriter()).thenReturn(writer);
        servlet.doPost(request,response);
//      start checking
        writer.flush();
        String ResponseString = stringWriter.toString();
//      check response object
        assertTrue(ResponseString.contains(username));
        Response res = JsonHelper.shared().fromJson(ResponseString, Response.class);
        assertTrue(res.getStatus());
        assertEquals(null,res.getMessage());
//      check databse
        assertNotNull(DatabaseManager.shared().checkUserExists(username));
    }
}