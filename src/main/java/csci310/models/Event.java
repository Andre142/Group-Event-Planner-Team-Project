package csci310.models;

import java.util.UUID;

public class Event {
    private String name;
    private String date;
    private String url;
    private String eventID;

    public void setName(String name) {
        this.name = name;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setEventID(String eventID) {
        this.eventID = eventID;
    }

    public String getName() {
        return name;
    }

    public String getDate() {
        return date;
    }

    public String getUrl() {
        return url;
    }

    public String getEventID() {
        return eventID;
    }

    public Event(String name, String date, String url) {
        this.name = name;
        this.date = date;
        this.url = url;
        eventID = UUID.randomUUID().toString();
    }

    public Event(String name, String date, String url, String eventID) {
        this.name = name;
        this.date = date;
        this.url = url;
        this.eventID = eventID;
    }

    public void generateID() {
        eventID = UUID.randomUUID().toString();
    }
}