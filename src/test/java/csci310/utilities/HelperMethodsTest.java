package csci310.utilities;

import org.junit.Test;

import java.io.IOException;
import java.security.SecureRandom;

import static org.junit.Assert.*;

public class HelperMethodsTest {

    @Test
    public void testGet_success() {
        String res = null;
        try {
            res = HelperMethods.get("https://www.google.com");
        } catch (IOException e) {}
        assertNotNull(res);
    }

    @Test
    public void testGet_fail() {
        String res = null;
        try {
            res = HelperMethods.get("https://www.google2.com");
        } catch (IOException e) {}
        assertNull(res);
    }

    @Test
    public void testShared() {
        assertTrue(HelperMethods.shared() != null);
    }

    @Test
    public void testGetSalt() {
        String salt = HelperMethods.getSalt();
        assertNotEquals(0,salt.length());
    }

    @Test
    public void testGetSHA512SecurePassword() {
        String psw = "abcdefgh";
        SecureRandom random = new SecureRandom();
        byte[] bytes = new byte[16];
        random.nextBytes(bytes);
        String salt = bytes.toString();
        String hashedPsw = HelperMethods.getSecurePassword(psw,salt,"SHA-512");
        assertTrue(hashedPsw.length() > 50);
        assertTrue(hashedPsw != psw);
    }

    @Test
    public void testGetSHA512SecurePassword_throwsException() {
        assertNull(HelperMethods.getSecurePassword("psw","salt","no"));
    }
}