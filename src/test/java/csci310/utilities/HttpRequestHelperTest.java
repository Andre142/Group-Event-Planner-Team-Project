package csci310.utilities;

import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.*;

public class HttpRequestHelperTest {

    @Test
    public void testGet_success() {
        new HttpRequestHelper();
        String res = "a";
        try {
            res = HttpRequestHelper.get("https://www.google.com");
        } catch (IOException e) {}
        assertNotNull(res);
    }

    @Test
    public void testGet_fail() {
        String res = null;
        try {
            res = HttpRequestHelper.get("https://www.google2.com");
        } catch (IOException e) {}
        assertNull(res);
    }
}