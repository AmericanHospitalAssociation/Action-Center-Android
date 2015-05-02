package org.aha.actioncenter.events;

import org.aha.actioncenter.models.NewsItem;

/**
 * Created by markusmcgee on 4/28/15.
 */
public class NewsDataEvent extends Event {
    public static final String NEWS_DATA = "NEWS_DATA";
    public static final String NEWS_LONG_DESCRIPTION_DATA = "NEWS_LONG_DESCRIPTION_DATA";

    private NewsItem item;

    public NewsDataEvent(String tagName){
        this.tagName = tagName;
    }

    public NewsDataEvent(String className, String message) {
        this.message = message; this.className = className;
    }

    public void setNewsItem(NewsItem item){
        this.item = item;
    }

    public NewsItem getNewsItem() {
        return item;
    }
}
