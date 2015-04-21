package org.aha.actioncenter.models;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by markusmcgee on 4/20/15.
 */
public class NavigationGroup {
    public String name;
    public String value;
    public HashMap<String, String> children = new HashMap<String, String>();

    public NavigationGroup(String name, String value) {
        this.name = name;
        this.value = value;
    }

}