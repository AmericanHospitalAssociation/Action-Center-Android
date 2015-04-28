package org.aha.actioncenter.events;

/**
 * Created by markusmcgee on 4/28/15.
 */
public class CampaignDataEvent extends Event {
    public static final String CAMPAIGN_DATA = "CAMPAIGN_DATA";

    public CampaignDataEvent(String tagName){
        this.tagName = tagName;
    }

    public CampaignDataEvent(String className, String message) {
        this.message = message; this.className = className;
    }
}
