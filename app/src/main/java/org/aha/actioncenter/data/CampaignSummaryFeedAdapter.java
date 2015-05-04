package org.aha.actioncenter.data;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.gson.Gson;

import org.aha.actioncenter.R;
import org.aha.actioncenter.models.CampaignSummaryItem;
import org.aha.actioncenter.views.CampaignSummaryDetailInfoFragment;

import java.util.List;


/**
 * Created by markusmcgee on 4/16/15.
 */

/**
 * Provide views to RecyclerView with data from mDataSet.
 */
public class CampaignSummaryFeedAdapter extends RecyclerView.Adapter<CampaignSummaryFeedAdapter.ViewHolder> {
    private static final String TAG = "CampaignSummaryFeedAdapter";

    private static List<CampaignSummaryItem> mDataSet;
    private static Activity mActivity;

    public CampaignSummaryFeedAdapter(Activity activity, List<CampaignSummaryItem> dataSet) {
        mActivity = activity;
        mDataSet = dataSet;
    }

    /**
     * Provide a reference to the type of views that you are using (custom ViewHolder)
     */
    public static class ViewHolder extends RecyclerView.ViewHolder {

        protected TextView title_txt = null;

        public ViewHolder(View v) {
            super(v);
            // Define click listener for the ViewHolder's View.
            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    int position = getAdapterPosition();

                    CampaignSummaryItem item = mDataSet.get(position);

                    Fragment fragment = new CampaignSummaryDetailInfoFragment();
                    Bundle args = new Bundle();
                    args.putString("item", new Gson().toJson(item));
                    fragment.setArguments(args);
                    FragmentManager fragmentManager = mActivity.getFragmentManager();
                    fragmentManager.beginTransaction().add(R.id.content_frame, fragment).addToBackStack(null).commit();

                }
            });
            title_txt = (TextView) v.findViewById(R.id.title_txt);
        }
    }

    // Create new views (invoked by the layout manager)
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        // Create a new view.
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.fact_sheet_item_view, viewGroup, false);

        return new ViewHolder(v);
    }


    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {
        CampaignSummaryItem item = mDataSet.get(position);
        viewHolder.title_txt.setText(item.headline);
    }


    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataSet.size();
    }
}
