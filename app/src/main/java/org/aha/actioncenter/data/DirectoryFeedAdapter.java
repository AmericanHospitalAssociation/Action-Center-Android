package org.aha.actioncenter.data;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.aha.actioncenter.R;
import org.aha.actioncenter.models.CampaignUserItem;

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

        public ViewHolder(View v) {
            super(v);
            // Define click listener for the ViewHolder's View.

            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d(TAG, "Element " + getAdapterPosition() + " clicked.");

                    /*
                    Fragment fragment = null;
                    Bundle args = new Bundle();

                    int position = getAdapterPosition();

                    CampaignUserItem item = mDataSet.get(position);

                    args.putString("item", new Gson().toJson(item));
                    fragment = new CampaignDetailInfoFragment();
                    fragment.setArguments(args);

                    // Insert the fragment by replacing any existing fragment
                    FragmentManager fragmentManager = mActivity.getFragmentManager();

                    fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).commit();
                    */
                }
            });

            name_txt = (TextView) v.findViewById(R.id.name_txt);

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