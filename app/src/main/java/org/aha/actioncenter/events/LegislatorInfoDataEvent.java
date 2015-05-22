package org.aha.actioncenter.events;

/**
 * Created by markusmcgee on 4/28/15.
 */
public class LegislatorInfoDataEvent extends Event {
    public static final String LEGISLATOR_DATA = "legislator_data";

    public LegislatorInfoDataEvent(String tagName){
        this.tagName = tagName;
    }

    public LegislatorInfoDataEvent(String className, String message) {
        this.message = message; this.className = className;
    }
}
