package org.aha.actioncenter.events;

/**
 * Created by markusmcgee on 4/21/15.
 */
public class ContactUsEvent extends Event {
    public static final String MESSAGE_SENT = "MESSAGE_SENT";

    public ContactUsEvent(String tagName){
        this.tagName = tagName;
    }

    public ContactUsEvent(String className, String message) {
        this.message = message;
        this.className = className;
    }
}
