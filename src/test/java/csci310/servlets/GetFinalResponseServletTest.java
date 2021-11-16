package csci310.servlets;

import org.junit.Test;

import static org.junit.Assert.*;

public class GetFinalResponseServletTest {

    @Test
    public void testDoGet() {
        GetFinalResponseServlet servlet = new GetFinalResponseServlet();
        assertNotNull(servlet);
    }
}