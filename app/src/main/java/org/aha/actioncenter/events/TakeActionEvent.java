package org.aha.actioncenter.events;

/**
 * Created by markusmcgee on 4/28/15.
 */
public class TakeActionEvent extends Event {
    public static final String TAKE_ACTION = "TAKE_ACTION";

    public TakeActionEvent(String tagName){
        this.tagName = tagName;
    }

    public TakeActionEvent(String className, String message) {
        this.message = message; this.className = className;
    }
}
