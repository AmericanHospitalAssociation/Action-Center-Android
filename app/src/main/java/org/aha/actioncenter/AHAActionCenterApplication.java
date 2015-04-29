package org.aha.actioncenter;

import android.app.Application;

import com.crashlytics.android.Crashlytics;
import com.parse.Parse;
import com.parse.ParseInstallation;
import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.TwitterAuthConfig;

import io.fabric.sdk.android.Fabric;

/**
 * Created by markusmcgee on 4/26/15.
 */
public class AHAActionCenterApplication extends Application {

    private boolean debug = false;

    @Override
    public void onCreate() {
        super.onCreate();
        if(!debug) {
            TwitterAuthConfig authConfig = new TwitterAuthConfig(getResources().getString(R.string.twitter_key), getResources().getString(R.string.twitter_secret));
            Fabric.with(this, new Crashlytics(), new Twitter(authConfig));

            Parse.initialize(this, "eCgr0cenQyGE8gAGe2i3HSR4TA9l3DwkBZWkJ5NI", "SWJUl9v413kjadIrowI8GucGsBhEuqLYGxUMPeDM");
            ParseInstallation.getCurrentInstallation().saveInBackground();
        }

    }
}
