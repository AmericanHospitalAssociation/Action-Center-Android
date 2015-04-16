package org.aha.actioncenter.events;

/**
 * Created by markusmcgee on 4/16/15.
 */
public class FeedDataEvent extends Event {
    public static final String FEED_DATA = "FEED_DATA";
    public static final String FEED_UPDATE_DATA = "FEED_UPDATE_DATA";

    public FeedDataEvent(String tagName){
        this.tagName = tagName;
    }

    public FeedDataEvent(String className, String message) {
        this.message = message; this.className = className;
    }


}
