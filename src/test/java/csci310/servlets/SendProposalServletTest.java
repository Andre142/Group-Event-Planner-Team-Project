package csci310.servlets;

import csci310.utilities.DatabaseManager;
import org.junit.Test;

import static org.junit.Assert.*;

public class SendProposalServletTest {

    // FIX ME !!!!!!
    @Test
    public void testDoPost() {
        String proposalID = "123456qwerty";
        String receiverUsername = "receiverJunitTester";
        DatabaseManager.shared().insertReceivedProposal(receiverUsername,proposalID);
        assertEquals(proposalID,DatabaseManager.shared().getProposalIDs(receiverUsername,false).get(0));
    }
}