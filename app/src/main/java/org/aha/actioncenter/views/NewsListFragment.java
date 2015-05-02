package org.aha.actioncenter.views;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableLayout;

import com.google.gson.Gson;
import com.squareup.otto.Subscribe;

import org.aha.actioncenter.R;
import org.aha.actioncenter.data.NewsFeedAdapter;
import org.aha.actioncenter.events.NewsDataEvent;
import org.aha.actioncenter.models.NewsItem;
import org.aha.actioncenter.utility.AHABusProvider;
import org.aha.actioncenter.utility.Utility;

import java.util.List;

/**
 * Created by markusmcgee on 4/17/15.
 */
public class NewsListFragment extends Fragment {

    private static final String TAG = "NewsListFragment";
    private TableLayout feedTable;
    private List<NewsItem> list;

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private Context mContext = null;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mContext = getActivity().getApplicationContext();
        View view = inflater.inflate(R.layout.feed_layout_view, container, false);
        //OttoBus must be registered after inflate.inflate or app blows up.
        AHABusProvider.getInstance().register(this);

        mRecyclerView = (RecyclerView) view.findViewById(R.id.feed_recycler_view);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(mContext);
        mRecyclerView.setLayoutManager(mLayoutManager);

        if (Utility.getInstance(mContext).isNewsDataLoaded()) {
            list = Utility.getInstance(mContext).getNewsData(Utility.getInstance().NEWS);
            mAdapter = new NewsFeedAdapter(getActivity(), list);
            mRecyclerView.setAdapter(mAdapter);
        }

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        AHABusProvider.getInstance().register(this);
        list = Utility.getInstance(mContext).getNewsData("aha-news");
    }

    @Override
    public void onPause() {
        super.onPause();
        AHABusProvider.getInstance().unregister(this);
    }

    @Subscribe
    public void subscribeOnNewsDataEvent(NewsDataEvent event) {
        if (event.getTagName().equals(NewsDataEvent.NEWS_LONG_DESCRIPTION_DATA)) {
            Fragment fragment = null;
            Bundle args = new Bundle();

            NewsItem item = event.getNewsItem();

            args.putString("item", new Gson().toJson(item));
            fragment = new NewsDetailInfoFragment();
            fragment.setArguments(args);

            // Insert the fragment by replacing any existing fragment
            FragmentManager fragmentManager = getActivity().getFragmentManager();

            fragmentManager.beginTransaction().add(R.id.content_frame, fragment).addToBackStack(null).commit();
        }
    }



}
