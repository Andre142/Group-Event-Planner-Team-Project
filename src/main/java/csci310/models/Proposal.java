package csci310.models;

import java.util.ArrayList;
import java.util.UUID;

public class Proposal {
    private String proposalTitle;
    private String senderUsername;
    private ArrayList<String> receiverUsernames;
    private ArrayList<Event> events;
    private String proposalID;

    public void setSenderUsername(String senderUsername) {
        this.senderUsername = senderUsername;
    }

    public void setReceiverUsernames(ArrayList<String> receiverUsernames) {
        this.receiverUsernames = receiverUsernames;
    }

    public void setEvents(ArrayList<Event> events) {
        this.events = events;
    }

    public void setProposalID(String proposalID) {
        this.proposalID = proposalID;
    }

    public void setProposalTitle(String proposalTitle) {
        this.proposalTitle = proposalTitle;
    }

    public String getSenderUsername() {
        return senderUsername;
    }

    public ArrayList<String> getReceiverUsernames() {
        return receiverUsernames;
    }

    public ArrayList<Event> getEvents() {
        return events;
    }

    public String getProposalID() {
        return proposalID;
    }

    public String getProposalTitle() {
        return proposalTitle;
    }

    public Proposal(String proposalTitle, String senderUsername, ArrayList<String> receiverUsernames, ArrayList<Event> events) {
        this.proposalTitle = proposalTitle;
        this.senderUsername = senderUsername;
        this.receiverUsernames = receiverUsernames;
        this.events = events;
        proposalID = UUID.randomUUID().toString();
    }

    public Proposal(String proposalTitle, String senderUsername, ArrayList<String> receiverUsernames, ArrayList<Event> events, String proposalID) {
        this.proposalTitle = proposalTitle;
        this.senderUsername = senderUsername;
        this.receiverUsernames = receiverUsernames;
        this.events = events;
        this.proposalID = proposalID;
    }

    public void generateID() {
        proposalID = UUID.randomUUID().toString();
    }

    public void generateIDForProposalAndEvents() {
        generateID();
        for (Event event: events) {
            event.generateID();
        }
    }
}
