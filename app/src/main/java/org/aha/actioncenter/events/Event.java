package org.aha.actioncenter.events;

import org.json.JSONObject;

/**
 * Created by markusmcgee on 4/16/15.
 */
public class Event {
    protected String tagName = "";
    protected String message = "DEFAULT MESSAGE";
    protected String className = "";
    protected JSONObject data = null;
    protected String dataString = "";


    public String getMessage() {
        return message;
    }
    public String getClassName() {
        return className;
    }

    public String getTagName(){
        return tagName;
    }

    public JSONObject getData() {
        return data;
    }

    public String getDataString(){
        return dataString;
    }

    public void setData(JSONObject data) {
        this.data = data;
    }

    public void setData(String val){
        this.dataString = val;
    }

    public boolean dataIsNull() {
        return (data==null);
    }
}

