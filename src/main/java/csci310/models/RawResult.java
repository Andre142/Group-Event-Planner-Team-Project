package csci310.models;

import java.util.ArrayList;

// class to parse api response

public class RawResult {
    EventList _embedded;
}

class EventList {
    ArrayList<aEvent> events;
}

class aEvent {
    String name;
    String url;
    Dates dates;
}

class Dates {
    Start start;
}

class Start {
    String localDate;
}
