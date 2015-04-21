package org.aha.actioncenter.models;

import java.util.ArrayList;

/**
 * Created by markusmcgee on 4/21/15.
 */
public class NavigationItem {
    public String id = "";
    public String name="";
    public ArrayList<NavigationItem> subnav = new ArrayList<NavigationItem>();
}
