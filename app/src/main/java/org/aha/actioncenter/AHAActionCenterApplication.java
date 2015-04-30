package org.aha.actioncenter;

import android.app.Activity;
import android.app.Application;
import android.os.AsyncTask;

import com.crashlytics.android.Crashlytics;
import com.parse.Parse;
import com.parse.ParseInstallation;
import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.TwitterAuthConfig;

import org.aha.actioncenter.service.EventsAsyncTask;
import org.aha.actioncenter.service.FeedAsyncTask;
import org.aha.actioncenter.service.NewsAsyncTask;

import java.net.MalformedURLException;
import java.net.URL;

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

    public void pullAdditionalData(Activity activity){
        URL feed_url = null;
        URL events_url = null;
        URL news_url = null;

        try {
            feed_url = new URL(getResources().getString(R.string.feed_url));
            FeedAsyncTask feedAsync = new FeedAsyncTask(feed_url, getApplicationContext(), activity);
            feedAsync.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

            events_url = new URL(getResources().getString(R.string.events_url));
            EventsAsyncTask eventAsync = new EventsAsyncTask(events_url, getApplicationContext(), activity);
            eventAsync.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

            news_url = new URL(getResources().getString(R.string.news_url));
            NewsAsyncTask newsAsyncTask = new NewsAsyncTask(news_url, getApplicationContext(), activity);
            newsAsyncTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        }
        catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }



}
