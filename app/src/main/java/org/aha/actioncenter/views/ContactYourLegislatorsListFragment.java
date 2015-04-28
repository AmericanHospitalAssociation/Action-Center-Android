package org.aha.actioncenter.views;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableLayout;

import com.squareup.otto.Subscribe;

import org.aha.actioncenter.R;
import org.aha.actioncenter.data.CampaignFeedAdapter;
import org.aha.actioncenter.events.CampaignDataEvent;
import org.aha.actioncenter.models.CampaignItem;
import org.aha.actioncenter.utility.AHABusProvider;
import org.aha.actioncenter.utility.Utility;

import java.util.List;

/**
 * Created by markusmcgee on 4/17/15.
 */
public class ContactYourLegislatorsListFragment extends Fragment {

    private static final String TAG = "ContactYourLegislatorsListFragment";
    private TableLayout feedTable;
    private List<CampaignItem> list;

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

        // specify an adapter (see also next example)
        if(Utility.getInstance(mContext).isFeedDataLoaded()) {
            list = Utility.getInstance(mContext).getCampaignData(Utility.getInstance().CONTACT_YOUR_LEGISLATORS);
            mAdapter = new CampaignFeedAdapter(getActivity(), list);
            mRecyclerView.setAdapter(mAdapter);
        }

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        AHABusProvider.getInstance().register(this);
        list = Utility.getInstance(mContext).getCampaignData(Utility.getInstance().CONTACT_YOUR_LEGISLATORS);
    }

    @Override
    public void onPause() {
        super.onPause();
        AHABusProvider.getInstance().unregister(this);
    }

    //Subscribe to Feed data event.  If data comes in update view.
    @Subscribe
    public void subscribeOnCampaignDataEvent(CampaignDataEvent event) {
        // specify an adapter (see also next example)
        if(Utility.getInstance(mContext).isCampaignDataLoaded()) {
            list = Utility.getInstance(mContext).getCampaignData(Utility.getInstance().CONTACT_YOUR_LEGISLATORS);
            mAdapter = new CampaignFeedAdapter(getActivity(), list);
            mRecyclerView.setAdapter(mAdapter);
        }
    }

}
