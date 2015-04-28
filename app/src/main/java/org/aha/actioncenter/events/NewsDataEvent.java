package org.aha.actioncenter.events;

/**
 * Created by markusmcgee on 4/28/15.
 */
public class NewsDataEvent extends Event {
    public static final String NEWS_DATA = "NEWS_DATA";

    public NewsDataEvent(String tagName){
        this.tagName = tagName;
    }

    public NewsDataEvent(String className, String message) {
        this.message = message; this.className = className;
    }
}
