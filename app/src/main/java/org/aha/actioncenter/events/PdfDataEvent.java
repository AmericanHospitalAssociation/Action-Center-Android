package org.aha.actioncenter.events;

/**
 * Created by markusmcgee on 4/16/15.
 */
public class PdfDataEvent extends Event {

    public static final String DOWNLOAD_DONE = "DOWNLOAD_DONE";

    public PdfDataEvent(String tagName){
        this.tagName = tagName;
    }

    public PdfDataEvent(String className, String message) {
        this.message = message; this.className = className;
    }


}
