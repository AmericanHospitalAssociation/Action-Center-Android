package org.aha.actioncenter.utility;

import com.squareup.otto.Bus;

/**
 * Created by markusmcgee on 4/16/15.
 */
public class AHABusProvider {
    private static final Bus INSTANCE  = new Bus();
    private AHABusProvider(){}

    public static Bus getInstance(){
        return INSTANCE;
    }


}

