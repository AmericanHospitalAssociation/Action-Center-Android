package org.aha.actioncenter.events;

/**
 * Created by markusmcgee on 4/21/15.
 */
public class LoginEvent extends Event {
    public static final String LOGIN_DATA = "LOGIN_DATA";
    public static final String LOGIN_UPDATE_DATA = "LOGIN_UPDATE_DATA";

    public LoginEvent(String tagName){
        this.tagName = tagName;
    }

    public LoginEvent(String className, String message) {
        this.message = message;
        this.className = className;
    }
}
