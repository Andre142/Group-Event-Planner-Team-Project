package csci310.models;

import java.util.ArrayList;

public class Events {
    ArrayList<Event> events;

    public Events(RawResult rawResult) throws NullPointerException {
        events = new ArrayList<>();
        for (aEvent aEvent: rawResult._embedded.events) {
            events.add(new Event(
                    aEvent.name,
                    aEvent.dates.start.localDate,
                    aEvent.url
            ));
        }
    }
}

class Event {
    private String name;
    private String date;
    private String url;

    public Event(String name, String date, String url) {
        this.name = name;
        this.date = date;
        this.url = url;
    }
}
