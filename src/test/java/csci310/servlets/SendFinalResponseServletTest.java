package csci310.servlets;

import org.junit.Test;

import static org.junit.Assert.*;

public class SendFinalResponseServletTest {

    @Test
    public void testDoPost() {
        SendFinalResponseServlet servlet = new SendFinalResponseServlet();
        assertNotNull(servlet);
    }
}