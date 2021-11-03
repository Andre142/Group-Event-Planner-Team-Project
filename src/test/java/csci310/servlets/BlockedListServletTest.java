package csci310.servlets;

import csci310.utilities.BlockedListDatabase;
import junit.framework.TestCase;
import org.junit.Test;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.sql.SQLException;

import static org.mockito.Mockito.*;

public class BlockedListServletTest extends TestCase {

    @Test
    public void testDoGet() throws IOException, SQLException {
        BlockedListDatabase.CreateDatabase();

        //default not blocked
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);

        when(request.getParameter("type")).thenReturn("getBlocked");
        when(request.getParameter("blocker")).thenReturn("userBlocker");
        when(request.getParameter("blockee")).thenReturn("userBlockee");
        when(request.getParameter("throw")).thenReturn("false");

        StringWriter stringWriter = new StringWriter();
        PrintWriter writer = new PrintWriter(stringWriter);
        when(response.getWriter()).thenReturn(writer);

        new BlockedListServlet().doGet(request, response);

        writer.flush();
        assertEquals("false", stringWriter.toString());

        //Test correctly getting a block
        BlockedListDatabase.AddBlock("userBlocker", "userBlockee");

        request = mock(HttpServletRequest.class);
        response = mock(HttpServletResponse.class);

        when(request.getParameter("type")).thenReturn("getBlocked");
        when(request.getParameter("blocker")).thenReturn("userBlocker");
        when(request.getParameter("blockee")).thenReturn("userBlockee");

        stringWriter = new StringWriter();
        writer = new PrintWriter(stringWriter);
        when(response.getWriter()).thenReturn(writer);

        new BlockedListServlet().doGet(request, response);

        writer.flush();
        assertEquals("true", stringWriter.toString());

        //Test removing block
        request = mock(HttpServletRequest.class);
        response = mock(HttpServletResponse.class);

        when(request.getParameter("type")).thenReturn("removeBlock");
        when(request.getParameter("blocker")).thenReturn("userBlocker");
        when(request.getParameter("blockee")).thenReturn("userBlockee");

        stringWriter = new StringWriter();
        writer = new PrintWriter(stringWriter);
        when(response.getWriter()).thenReturn(writer);

        new BlockedListServlet().doGet(request, response);

        request = mock(HttpServletRequest.class);
        response = mock(HttpServletResponse.class);

        when(request.getParameter("type")).thenReturn("getBlocked");
        when(request.getParameter("blocker")).thenReturn("userBlocker");
        when(request.getParameter("blockee")).thenReturn("userBlockee");

        stringWriter = new StringWriter();
        writer = new PrintWriter(stringWriter);
        when(response.getWriter()).thenReturn(writer);

        new BlockedListServlet().doGet(request, response);

        writer.flush();
        assertEquals("false", stringWriter.toString());

        //Test adding block
        request = mock(HttpServletRequest.class);
        response = mock(HttpServletResponse.class);

        when(request.getParameter("type")).thenReturn("addBlock");
        when(request.getParameter("blocker")).thenReturn("userBlocker");
        when(request.getParameter("blockee")).thenReturn("userBlockee");

        stringWriter = new StringWriter();
        writer = new PrintWriter(stringWriter);
        when(response.getWriter()).thenReturn(writer);

        new BlockedListServlet().doGet(request, response);

        request = mock(HttpServletRequest.class);
        response = mock(HttpServletResponse.class);

        when(request.getParameter("type")).thenReturn("getBlocked");
        when(request.getParameter("blocker")).thenReturn("userBlocker");
        when(request.getParameter("blockee")).thenReturn("userBlockee");

        stringWriter = new StringWriter();
        writer = new PrintWriter(stringWriter);
        when(response.getWriter()).thenReturn(writer);

        new BlockedListServlet().doGet(request, response);

        writer.flush();
        assertEquals("true", stringWriter.toString());

        //Test getting block list
        request = mock(HttpServletRequest.class);
        response = mock(HttpServletResponse.class);

        when(request.getParameter("type")).thenReturn("addBlock");
        when(request.getParameter("blocker")).thenReturn("userBlocker");
        when(request.getParameter("blockee")).thenReturn("userBlockee2");

        stringWriter = new StringWriter();
        writer = new PrintWriter(stringWriter);
        when(response.getWriter()).thenReturn(writer);

        new BlockedListServlet().doGet(request, response);

        request = mock(HttpServletRequest.class);
        response = mock(HttpServletResponse.class);

        when(request.getParameter("type")).thenReturn("getBlockList");
        when(request.getParameter("blocker")).thenReturn("userBlocker");

        stringWriter = new StringWriter();
        writer = new PrintWriter(stringWriter);
        when(response.getWriter()).thenReturn(writer);

        new BlockedListServlet().doGet(request, response);

        writer.flush();
        assertEquals("userBlockee,userBlockee2", stringWriter.toString());

        request = mock(HttpServletRequest.class);
        response = mock(HttpServletResponse.class);

        when(request.getParameter("type")).thenReturn("hello");
        when(request.getParameter("blocker")).thenReturn("userBlocker");

        stringWriter = new StringWriter();
        writer = new PrintWriter(stringWriter);
        when(response.getWriter()).thenReturn(writer);

        new BlockedListServlet().doGet(request, response);

        writer.flush();
        assertEquals("invalid type", stringWriter.toString());

        request = mock(HttpServletRequest.class);
        response = mock(HttpServletResponse.class);

        when(request.getParameter("throw")).thenReturn("true");

        stringWriter = new StringWriter();
        writer = new PrintWriter(stringWriter);
        when(response.getWriter()).thenReturn(writer);

        new BlockedListServlet().doGet(request, response);

        writer.flush();
        assertEquals("exception occurred", stringWriter.toString());
    }
}