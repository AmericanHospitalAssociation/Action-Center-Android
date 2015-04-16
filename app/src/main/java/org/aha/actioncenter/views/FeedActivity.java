package org.aha.actioncenter.views;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.google.gson.Gson;
import com.squareup.otto.Subscribe;

import org.aha.actioncenter.R;
import org.aha.actioncenter.events.FeedDataEvent;
import org.aha.actioncenter.service.FeedAsyncTask;
import org.aha.actioncenter.utility.AHABusProvider;
import org.aha.actioncenter.utility.Utility;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by markusmcgee on 4/15/15.
 */
public class FeedActivity extends Activity {

    private static final String TAG = "FeedActivity";
    private TableLayout feedTable;
    private JSONArray jArray;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.feed_layout_view);

        AHABusProvider.getInstance().register(this);

        String intentData = getIntent().getStringExtra("data");
        loadProcessViewData(intentData);
    }

    private void loadProcessViewData(String jsonString){

        try {
            jArray = new JSONArray(jsonString);
            updateView();
        }
        catch (JSONException e) {
            e.printStackTrace();
        }

    }

    private void updateView() {

        if (feedTable == null)
            feedTable = (TableLayout) findViewById(R.id.feed_table);

        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        int count = jArray.length();

        for (int i = 0; i < count; i++) {

            JSONObject row_data = null;

            TableRow row = (TableRow) inflater.inflate(R.layout.feed_item_view, null);

            // TextView wp_content = (TextView)
            // row.findViewById(R.id.wp_content);
            TextView wp_date = (TextView) row.findViewById(R.id.feed_date);

            try {
                row_data = jArray.getJSONObject(i);
            }
            catch (JSONException e) {
                e.printStackTrace();
            }

            // String _content = row_data.isNull("content") ? "" :
            // row_data.getString("content");
            // wp_content.setText(_content);

            row.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    Log.d(TAG, "Row Clicked");
                }
            });

            feedTable.addView(row, 0);

        }

        Log.d(TAG, "break point debug");
    }


    @Override
    protected void onResume() {
        super.onResume();
        AHABusProvider.getInstance().register(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        AHABusProvider.getInstance().unregister(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        AHABusProvider.getInstance().unregister(this);
    }

    private void refreshFeedData(){
        try {
            URL url = new URL(getResources().getString(R.string.feed_url));
            Context context = getApplicationContext();
            FeedAsyncTask feedAsync = new FeedAsyncTask(url, context);
            feedAsync.execute();
        }
        catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    //Subscribe to Feed data event.  If data comes in update view.
    @Subscribe
    public void subscribeOnFeedDataEvent(FeedDataEvent event) {

        try {
            jArray = (JSONArray)event.getData().getJSONArray("FEED_PAYLOAD");
            Utility.getInstance(getApplicationContext()).parseFeedData(jArray);
        }
        catch (JSONException e) {
            e.printStackTrace();
        }

        updateView();

    }

}
