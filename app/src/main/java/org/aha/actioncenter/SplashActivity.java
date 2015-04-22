package org.aha.actioncenter;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.crashlytics.android.Crashlytics;
import com.squareup.otto.Subscribe;
import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.TwitterAuthConfig;

import org.aha.actioncenter.events.FeedDataEvent;
import org.aha.actioncenter.service.FeedAsyncTask;
import org.aha.actioncenter.utility.AHABusProvider;
import org.aha.actioncenter.utility.Utility;
import org.json.JSONArray;
import org.json.JSONException;

import java.net.MalformedURLException;
import java.net.URL;

import io.fabric.sdk.android.Fabric;


public class SplashActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.splash_screen_view);
        AHABusProvider.getInstance().register(this);

        TwitterAuthConfig authConfig = new TwitterAuthConfig(getResources().getString(R.string.twitter_key), getResources().getString(R.string.twitter_secret));
        Fabric.with(this, new Crashlytics(), new Twitter(authConfig));

        URL url = null;
        try {
            url = new URL(getResources().getString(R.string.feed_url));
        }
        catch (MalformedURLException e) {
            e.printStackTrace();
        }


        FeedAsyncTask asyncTask = new FeedAsyncTask(url, getApplicationContext(), this);
        asyncTask.execute();

    }

    //Subscribe to Feed data event.  This should probably listen only once then unregister it self.
    //MainActivity should only be shown once?
    @Subscribe
    public void subscribeOnFeedDataEvent(FeedDataEvent event) {
        try {
            JSONArray jArray = null;
            jArray = (JSONArray) event.getData().getJSONArray("FEED_PAYLOAD");
            Utility.getInstance(getApplicationContext()).parseFeedData(jArray);

            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);

        }
        catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        AHABusProvider.getInstance().unregister(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        AHABusProvider.getInstance().register(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_splash, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
