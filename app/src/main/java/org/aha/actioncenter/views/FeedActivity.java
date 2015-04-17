package org.aha.actioncenter.views;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TableLayout;

import com.squareup.otto.Subscribe;

import org.aha.actioncenter.R;
import org.aha.actioncenter.events.FeedDataEvent;
import org.aha.actioncenter.models.FeedItem;
import org.aha.actioncenter.service.FeedAsyncTask;
import org.aha.actioncenter.utility.AHABusProvider;
import org.aha.actioncenter.utility.Utility;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

/**
 * Created by markusmcgee on 4/15/15.
 */
public class FeedActivity extends Activity {

    private static final String TAG = "FeedActivity";
    private TableLayout feedTable;
    private List<FeedItem> list;

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.feed_layout_view);

        //OttoBus must be registered after setContentView or app blows up.
        AHABusProvider.getInstance().register(this);

        mRecyclerView = (RecyclerView) findViewById(R.id.feed_recycler_view);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        // specify an adapter (see also next example)
        list = Utility.getInstance(getApplicationContext()).getFeedData("action-alert");
        mAdapter = new ActionAlertFeedAdapter(list);
        mRecyclerView.setAdapter(mAdapter);


    }

    @Override
    protected void onResume() {
        super.onResume();
        AHABusProvider.getInstance().register(this);

        list = Utility.getInstance(getApplicationContext()).getFeedData("action-alert");

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

    private void refreshFeedData() {
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

        list = Utility.getInstance(getApplicationContext()).getFeedData("action-alert");


    }

}
