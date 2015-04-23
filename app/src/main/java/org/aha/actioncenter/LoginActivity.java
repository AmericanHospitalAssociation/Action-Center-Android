package org.aha.actioncenter;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.squareup.otto.Subscribe;

import org.aha.actioncenter.events.FeedDataEvent;
import org.aha.actioncenter.service.FeedAsyncTask;
import org.aha.actioncenter.utility.AHABusProvider;
import org.aha.actioncenter.utility.Utility;
import org.json.JSONArray;
import org.json.JSONException;

import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by markusmcgee on 4/23/15.
 */
public class LoginActivity extends Activity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.login_fragment_view);
        AHABusProvider.getInstance().register(this);

        Button button = (Button) findViewById(R.id.login_btn);
        button.setOnClickListener(this);

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
    public void onClick(View view) {


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

            //SharedPreferences prefs = getApplicationContext().getSharedPreferences("login", Context.MODE_PRIVATE);
            //boolean isValidLogin = prefs.getBoolean("login", false);

            Intent intent;
            intent = new Intent(this, MainActivity.class);
            startActivity(intent);

        }
        catch (JSONException e) {
            e.printStackTrace();
        }
    }
}