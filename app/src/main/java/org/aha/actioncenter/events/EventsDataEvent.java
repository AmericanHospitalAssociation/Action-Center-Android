package org.aha.actioncenter.events;

/**
 * Created by markusmcgee on 4/16/15.
 */
public class EventsDataEvent extends Event {
    public static final String EVENTS_DATA = "EVENTS_DATA";
    public static final String EVENTS_UPDATE_DATA = "EVENTS_UPDATE_DATA";

    public EventsDataEvent(String tagName){
        this.tagName = tagName;
    }

    public EventsDataEvent(String className, String message) {
        this.message = message; this.className = className;
    }


}
