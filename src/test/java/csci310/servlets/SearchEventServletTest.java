package csci310.servlets;

import csci310.models.Response;
import csci310.utilities.JsonHelper;
import org.junit.Before;
import org.junit.Test;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class SearchEventServletTest {
    private SearchEventServlet servlet;

    @Before
    public void setUp() {
        servlet = new SearchEventServlet();
    }

    @Test
    public void testdoGet_success() throws IOException {
        HttpServletRequest req = mock(HttpServletRequest.class);
        HttpServletResponse res = mock(HttpServletResponse.class);
        when(req.getParameter("keyword")).thenReturn("music");
        StringWriter stringWriter = new StringWriter();
        PrintWriter writer = new PrintWriter(stringWriter);
        when(res.getWriter()).thenReturn(writer);
        servlet.doGet(req,res);
        writer.flush();
        Response response = JsonHelper.shared().fromJson(stringWriter.toString(),Response.class);
        assertTrue(response.getStatus());
        assertNull(response.getMessage());
        assertNotNull(response.getData());
    }

    @Test
    public void testdoGet_failEmptyResult() throws IOException {
        HttpServletRequest req = mock(HttpServletRequest.class);
        HttpServletResponse res = mock(HttpServletResponse.class);
        when(req.getParameter("startDate")).thenReturn("2021-01-03");
        when(req.getParameter("endDate")).thenReturn("2021-01-04");
        StringWriter stringWriter = new StringWriter();
        PrintWriter writer = new PrintWriter(stringWriter);
        when(res.getWriter()).thenReturn(writer);
        servlet.doGet(req,res);
        writer.flush();
        Response response = JsonHelper.shared().fromJson(stringWriter.toString(),Response.class);
        assertFalse(response.getStatus());
        assertEquals("No results returned for the query",response.getMessage());
        assertNull(response.getData());
    }

    @Test
    public void testdoGet_failInvalidDateRange() throws IOException {
        HttpServletRequest req = mock(HttpServletRequest.class);
        HttpServletResponse res = mock(HttpServletResponse.class);
        when(req.getParameter("startDate")).thenReturn("2021-01-00");
        when(req.getParameter("endDate")).thenReturn("2021-01-01");
        StringWriter stringWriter = new StringWriter();
        PrintWriter writer = new PrintWriter(stringWriter);
        when(res.getWriter()).thenReturn(writer);
        servlet.doGet(req,res);
        writer.flush();
        Response response = JsonHelper.shared().fromJson(stringWriter.toString(),Response.class);
        assertFalse(response.getStatus());
        assertEquals("No results returned for the query",response.getMessage());
        assertNull(response.getData());
    }

    @Test
    public void testdoGet_successEndDateAndCountryCode() throws IOException {
        HttpServletRequest req = mock(HttpServletRequest.class);
        HttpServletResponse res = mock(HttpServletResponse.class);
        when(req.getParameter("endDate")).thenReturn("2021-01-01");
        when(req.getParameter("countryCode")).thenReturn("US");
        StringWriter stringWriter = new StringWriter();
        PrintWriter writer = new PrintWriter(stringWriter);
        when(res.getWriter()).thenReturn(writer);
        servlet.doGet(req,res);
        writer.flush();
        Response response = JsonHelper.shared().fromJson(stringWriter.toString(),Response.class);
        assertTrue(response.getStatus());
        assertNull(response.getMessage());
        assertNotNull(response.getData());
    }
}