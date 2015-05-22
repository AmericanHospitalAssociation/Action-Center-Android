package org.aha.actioncenter.models;

import java.util.ArrayList;

/**
 * Created by markusmcgee on 5/21/15.
 */
public class LegislatorItem {

    public String displayName;
    public String photoUrl;
    public ArrayList<Sections> sections;

    public class Sections {
        public String name;
        public ArrayList<Properties> properties;
    }

    public class Properties {
        public String description;
        public String name;
        public String value;

    }
}




