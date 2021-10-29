package csci310.models;

import java.util.ArrayList;
import java.util.UUID;

public class Proposal {
    private String senderUsername;
    private ArrayList<String> receiverUsername;
    private ArrayList<Event> events;
    private String proposalID;

    public void setSenderUsername(String senderUsername) {
        this.senderUsername = senderUsername;
    }

    public void setReceiverUsername(ArrayList<String> receiverUsername) {
        this.receiverUsername = receiverUsername;
    }

    public void setEvents(ArrayList<Event> events) {
        this.events = events;
    }

    public void setProposalID(String proposalID) {
        this.proposalID = proposalID;
    }

    public String getSenderUsername() {
        return senderUsername;
    }

    public ArrayList<String> getReceiverUsername() {
        return receiverUsername;
    }

    public ArrayList<Event> getEvents() {
        return events;
    }

    public String getProposalID() {
        return proposalID;
    }

    public Proposal(String senderUsername, ArrayList<String> receiverUsername, ArrayList<Event> events) {
        this.senderUsername = senderUsername;
        this.receiverUsername = receiverUsername;
        this.events = events;
        proposalID = UUID.randomUUID().toString();
    }

    public Proposal(String senderUsername, ArrayList<String> receiverUsername, ArrayList<Event> events, String proposalID) {
        this.senderUsername = senderUsername;
        this.receiverUsername = receiverUsername;
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
