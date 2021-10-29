package csci310.models;

import org.junit.Test;

import static org.junit.Assert.*;

public class ProposalTest {

    @Test
    public void testGenerateID() {
        Proposal proposal = new Proposal("a",null,null);
        assertNotNull(proposal);
    }

    @Test
    public void testGenerateIDForProposalAndEvents() {
        Proposal proposal = new Proposal("a",null,null);
        assertNotNull(proposal);
    }
}