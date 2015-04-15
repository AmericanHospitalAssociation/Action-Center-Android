package org.aha.actioncenter.views;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import org.aha.actioncenter.R;
import org.aha.actioncenter.service.FeedAsyncTask;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by markusmcgee on 4/15/15.
 */
public class FeedActivity extends Activity implements FeedCallBack {

    private static final String TAG = "FeedActivity";
    private TableLayout feedTable;
    private JSONArray jArray;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.feed_layout_view);
        feedTable = (TableLayout) findViewById(R.id.feed_table);

        //Test to pull Feed
        try {
            URL url = new URL(getResources().getString(R.string.feed_url));
            Context context = getApplicationContext();
            FeedAsyncTask feedAsync = new FeedAsyncTask(this, url, context);
            feedAsync.execute();
        }
        catch (MalformedURLException e) {
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
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    public void feedCallBackDone(JSONArray jArray) {
        this.jArray = jArray;
        updateView();
    }
}
