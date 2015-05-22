package org.aha.actioncenter.data;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.gson.Gson;

import org.aha.actioncenter.MainActivity;
import org.aha.actioncenter.R;
import org.aha.actioncenter.models.CampaignUserItem;
import org.aha.actioncenter.service.LegislatorInfoAsyncTask;
import org.aha.actioncenter.views.CampaignSummaryListFragment;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;


/**
 * Created by markusmcgee on 4/16/15.
 */

/**
 * Provide views to RecyclerView with data from mDataSet.
 */
public class DirectoryFeedAdapter extends RecyclerView.Adapter<DirectoryFeedAdapter.ViewHolder> {
    private static final String TAG = "DirectoryFeedAdapter";

    private static List<CampaignUserItem> mDataSet;
    private static Activity mActivity;

    public DirectoryFeedAdapter(Activity activity, List<CampaignUserItem> dataSet) {
        mActivity = activity;
        mDataSet = dataSet;

    }

    /**
     * Provide a reference to the type of views that you are using (custom ViewHolder)
     */
    public static class ViewHolder extends RecyclerView.ViewHolder {

        protected TextView name_txt = null;
        protected Button take_action_btn = null;

        public ViewHolder(View v) {
            super(v);
            // Define click listener for the ViewHolder's View.

            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d(TAG, "Element " + getAdapterPosition() + " clicked.");

                    int position = getAdapterPosition();
                    CampaignUserItem item = mDataSet.get(position);

                    if(item != null) {
                        try {
                            String urlString;
                            urlString = mActivity.getString(R.string.vv_get_us_profile_url);
                            urlString = urlString.replace("mId", item.id);
                            urlString = urlString.replace("mType", item.type);
                            URL url = new URL(urlString);
                            new LegislatorInfoAsyncTask(url, mActivity.getApplicationContext(), mActivity).execute();
                        }
                        catch (MalformedURLException e) {
                            e.printStackTrace();
                        }
                    }

                }
            });

            name_txt = (TextView) v.findViewById(R.id.name_txt);
            take_action_btn = (Button) v.findViewById(R.id.take_action_btn);
            if (take_action_btn != null) {
                take_action_btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Fragment fragment;
                        Bundle args = new Bundle();

                        int position = getAdapterPosition();

                        CampaignUserItem item = mDataSet.get(position);

                        args.putString("item", new Gson().toJson(item));
                        fragment = new CampaignSummaryListFragment();
                        fragment.setArguments(args);

                        ((MainActivity) mActivity).addToAppBackStack(fragment, "campaign-summary", "Campaigns");
                    }
                });
            }
        }
    }

    @Override
    public int getItemViewType(int position) {
        CampaignUserItem item = mDataSet.get(position);
        return (item.isHeader ? 0 : 1);
    }

    // Create new views (invoked by the layout manager)
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        // Create a new view.

        View v;
        if (viewType == 0)
            v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.directory_header_item_view, viewGroup, false);
        else
            v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.directory_item_view, viewGroup, false);

        return new ViewHolder(v);
    }


    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {

        CampaignUserItem item = mDataSet.get(position);

        viewHolder.name_txt.setText(item.name);
    }


    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataSet.size();
    }

}