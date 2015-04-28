package org.aha.actioncenter.views;

import android.app.Fragment;
import android.app.ListFragment;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TableLayout;

import com.squareup.otto.Subscribe;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.models.Tweet;
import com.twitter.sdk.android.tweetui.CompactTweetView;
import com.twitter.sdk.android.tweetui.LoadCallback;
import com.twitter.sdk.android.tweetui.TweetTimelineListAdapter;
import com.twitter.sdk.android.tweetui.TweetViewAdapter;
import com.twitter.sdk.android.tweetui.TweetViewFetchAdapter;
import com.twitter.sdk.android.tweetui.UserTimeline;

import org.aha.actioncenter.MainActivity;
import org.aha.actioncenter.R;
import org.aha.actioncenter.data.ActionAlertFeedAdapter;
import org.aha.actioncenter.events.FeedDataEvent;
import org.aha.actioncenter.models.FeedItem;
import org.aha.actioncenter.service.FeedAsyncTask;
import org.aha.actioncenter.utility.AHABusProvider;
import org.aha.actioncenter.utility.Utility;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.List;

/**
 * Created by markusmcgee on 4/26/15.
 */
public class TwitterFeedListFragment extends ListFragment {

    private static final String TAG = "TwitterFeedListFragment";
    private Context mContext = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle bundle = getArguments();

        UserTimeline userTimeline = new UserTimeline.Builder().screenName(bundle.getString(Utility.getInstance().TWITTER_FEEDS)).build();
        TweetTimelineListAdapter adapter = new TweetTimelineListAdapter(getActivity(), userTimeline);

        setListAdapter(adapter);

        //setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mContext = getActivity().getApplicationContext();
        return inflater.inflate(R.layout.tweet_list, container, false);
    }

/*

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater = getActivity().getMenuInflater();
        inflater.inflate(R.menu.detail_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.close_detail_action) {
            getFragmentManager().popBackStack();
        }
        return super.onOptionsItemSelected(item);
    }
*/

    @Override
    public void onResume() {
        super.onResume();
    }
}