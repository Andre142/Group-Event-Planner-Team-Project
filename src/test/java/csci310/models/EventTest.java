package csci310.models;

import org.junit.Test;

import static org.junit.Assert.*;

public class EventTest {

    @Test
    public void testGenerateID() {
        Event event = new Event("a","a","a");
        assertNotNull(event);
    }
}