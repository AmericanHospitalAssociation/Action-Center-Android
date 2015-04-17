package org.aha.actioncenter.views;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.aha.actioncenter.R;
import org.aha.actioncenter.models.FeedItem;

import java.util.List;


/**
 * Created by markusmcgee on 4/16/15.
 */

/**
 * Provide views to RecyclerView with data from mDataSet.
 */
public class ActionAlertFeedAdapter extends RecyclerView.Adapter<ActionAlertFeedAdapter.ViewHolder> {
    private static final String TAG = "ActionAlertFeedAdapter";

    private List<FeedItem> mDataSet;

    public ActionAlertFeedAdapter(List<FeedItem> dataSet) {
        mDataSet = dataSet;
    }

    /**
     * Provide a reference to the type of views that you are using (custom ViewHolder)
     */
    public static class ViewHolder extends RecyclerView.ViewHolder {

        protected TextView title_txt = null;
        protected TextView action_from_txt = null;
        protected TextView action_needed_txt = null;
        protected TextView when_c_txt = null;
        protected TextView why_txt = null;

        public ViewHolder(View v) {
            super(v);
            // Define click listener for the ViewHolder's View.
            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d(TAG, "Element " + getPosition() + " clicked.");
                }
            });

            title_txt = (TextView) v.findViewById(R.id.title_txt);
            action_from_txt = (TextView) v.findViewById(R.id.action_from_txt);
            action_needed_txt = (TextView) v.findViewById(R.id.action_needed_txt);
            when_c_txt = (TextView) v.findViewById(R.id.when_c_txt);
            why_txt = (TextView) v.findViewById(R.id.why_txt);

        }
    }

    // Create new views (invoked by the layout manager)
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        // Create a new view.
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.feed_item_view, viewGroup, false);

        return new ViewHolder(v);
    }


    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {
        Log.d(TAG, "Element " + position + " set.");

        FeedItem item = mDataSet.get(position);

        viewHolder.title_txt.setText(item.Title);
        viewHolder.action_from_txt.setText(item.ActionFrom);
        viewHolder.action_needed_txt.setText(item.ActionNeeded);
        viewHolder.when_c_txt.setText(item.When_c);
        viewHolder.why_txt.setText(item.Why);
    }


    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataSet.size();
    }
}
