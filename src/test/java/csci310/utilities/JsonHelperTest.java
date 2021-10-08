package csci310.utilities;

import org.junit.Test;

import static org.junit.Assert.*;

public class JsonHelperTest {

    @Test
    public void testshared() {
        new JsonHelper();
        assertTrue(JsonHelper.shared() != null);
    }
}