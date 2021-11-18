package csci310.utilities;

import org.junit.Test;

import java.io.IOException;
import java.security.SecureRandom;

import static org.junit.Assert.*;

public class HelperMethodsTest {

    @Test
    public void testGet_success() {
        new HttpRequestHelper();
        String res = null;
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

    @Test
    public void testShared() {
        new JsonHelper();
        assertTrue(JsonHelper.shared() != null);
    }

    @Test
    public void testGetSalt() {
        String salt = SecurePasswordHelper.getSalt();
        assertNotEquals(0,salt.length());
    }

    @Test
    public void testGetSHA512SecurePassword() {
        new SecurePasswordHelper();
        String psw = "abcdefgh";
        SecureRandom random = new SecureRandom();
        byte[] bytes = new byte[16];
        random.nextBytes(bytes);
        String salt = bytes.toString();
        String hashedPsw = SecurePasswordHelper.getSecurePassword(psw,salt,"SHA-512");
        assertTrue(hashedPsw.length() > 50);
        assertTrue(hashedPsw != psw);
    }

    @Test
    public void testGetSHA512SecurePassword_throwsException() {
        assertNull(SecurePasswordHelper.getSecurePassword("psw","salt","no"));
    }
}