package csci310.utilities;

import org.junit.Test;

import java.security.SecureRandom;

import static org.junit.Assert.*;

public class SecurePasswordHelperTest {

    @Test
    public void getSalt() {
        String salt = SecurePasswordHelper.getSalt();
        assertNotEquals(0,salt.length());
    }

    @Test
    public void testgetSHA512SecurePassword() {
        new SecurePasswordHelper();
        String psw = "abcdefgh";
        SecureRandom random = new SecureRandom();
        byte[] bytes = new byte[16];
        random.nextBytes(bytes);
        String salt = bytes.toString();
        String hashedPsw = SecurePasswordHelper.getSHA512SecurePassword(psw,salt);
        assertTrue(hashedPsw.length() > 50);
        assertTrue(hashedPsw != psw);
    }
}