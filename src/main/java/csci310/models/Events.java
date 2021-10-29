package csci310.models;

import java.util.ArrayList;

public class Events {

    private ArrayList<Event> events;

    public ArrayList<Event> getEvents() {
        return events;
    }

    public void setEvents(ArrayList<Event> events) {
        this.events = events;
    }

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
