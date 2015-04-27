package org.aha.actioncenter.views;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TableLayout;

import com.squareup.otto.Subscribe;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.models.Tweet;
import com.twitter.sdk.android.tweetui.CompactTweetView;
import com.twitter.sdk.android.tweetui.LoadCallback;
import com.twitter.sdk.android.tweetui.TweetViewAdapter;
import com.twitter.sdk.android.tweetui.TweetViewFetchAdapter;

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
public class TwitterFeedListFragment extends Fragment {

    private static final String TAG = "TwitterFeedListFragment";
    private List<FeedItem> list;

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private Context mContext = null;

    List<Long> tweetIds = Arrays.asList(503435417459249153L, 510908133917487104L, 473514864153870337L, 477788140900347904L);
    final TweetViewFetchAdapter adapter = new TweetViewFetchAdapter<CompactTweetView>(getActivity());

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mContext = getActivity().getApplicationContext();
        View view = inflater.inflate(R.layout.tweet_list, container, false);
        ListView list = (ListView) view.findViewById(R.id.twitter_list_view);

        list.setAdapter(adapter);

        adapter.setTweetIds(tweetIds, new LoadCallback<List<Tweet>>() {
            @Override
            public void success(List<Tweet> tweets) {
                // my custom actions
                Log.d(TAG, "debug");
            }

            @Override
            public void failure(TwitterException exception) {
                // Toast.makeText(...).show();
                Log.d(TAG, "debug");
            }
        });

        return view;
    }

}