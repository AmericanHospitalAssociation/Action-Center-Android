package org.aha.actioncenter.data;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.gson.Gson;

import org.aha.actioncenter.R;
import org.aha.actioncenter.models.EventItem;
import org.aha.actioncenter.views.EventDetailInfoFragment;

import java.util.List;


/**
 * Created by markusmcgee on 4/16/15.
 */

/**
 * Provide views to RecyclerView with data from mDataSet.
 */
public class EventsFeedAdapter extends RecyclerView.Adapter<EventsFeedAdapter.ViewHolder> {
    private static final String TAG = "EventsFeedAdapter";

    private static List<EventItem> mDataSet;
    private static Activity mActivity;

    public EventsFeedAdapter(Activity activity, List<EventItem> dataSet) {
        mActivity = activity;
        mDataSet = dataSet;
    }

    /**
     * Provide a reference to the type of views that you are using (custom ViewHolder)
     */
    public static class ViewHolder extends RecyclerView.ViewHolder {

        protected TextView clean_title = null;
        protected TextView pretty_date = null;
        protected TextView meeting_time = null;

        public ViewHolder(View v) {
            super(v);
            // Define click listener for the ViewHolder's View.

            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d(TAG, "Element " + getAdapterPosition() + " clicked.");

                    Fragment fragment = null;
                    Bundle args = new Bundle();

                    int position = getAdapterPosition();

                    EventItem item = mDataSet.get(position);

                    args.putString("item", new Gson().toJson(item));
                    fragment = new EventDetailInfoFragment();
                    fragment.setArguments(args);

                    // Insert the fragment by replacing any existing fragment
                    FragmentManager fragmentManager = mActivity.getFragmentManager();
                    fragmentManager.beginTransaction().add(R.id.content_frame, fragment).addToBackStack(null).commit();

                }
            });

            clean_title = (TextView) v.findViewById(R.id.clean_title);
            pretty_date = (TextView) v.findViewById(R.id.pretty_date);
            meeting_time = (TextView) v.findViewById(R.id.meeting_time);


        }
    }

    // Create new views (invoked by the layout manager)
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        // Create a new view.
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.calendar_item_view, viewGroup, false);

        return new ViewHolder(v);
    }


    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {

        EventItem item = mDataSet.get(position);

        viewHolder.clean_title.setText(item.clean_title);
        viewHolder.pretty_date.setText(item.pretty_date);
        viewHolder.meeting_time.setText(item.meeting_start_time + " - " + item.meeting_end_time);
    }


    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataSet.size();
    }
}