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
import org.aha.actioncenter.models.FeedItem;
import org.aha.actioncenter.views.TestimonyDetailInfoFragment;

import java.util.List;


/**
 * Created by markusmcgee on 4/16/15.
 */

/**
 * Provide views to RecyclerView with data from mDataSet.
 */
public class TestimonyFeedAdapter extends RecyclerView.Adapter<TestimonyFeedAdapter.ViewHolder> {
    private static final String TAG = "TestimonyFeedAdapter";

    private static List<FeedItem> mDataSet;
    private static Activity mActivity;

    public TestimonyFeedAdapter(Activity activity, List<FeedItem> dataSet) {
        mActivity = activity;
        mDataSet = dataSet;
    }

    /**
     * Provide a reference to the type of views that you are using (custom ViewHolder)
     */
    public static class ViewHolder extends RecyclerView.ViewHolder {

        protected TextView title_txt = null;
        protected TextView date_txt = null;
        protected TextView description_txt = null;

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

                    FeedItem item = mDataSet.get(position);

                    args.putString("item", new Gson().toJson(item));
                    fragment = new TestimonyDetailInfoFragment();
                    fragment.setArguments(args);

                    // Insert the fragment by replacing any existing fragment
                    FragmentManager fragmentManager = mActivity.getFragmentManager();

                    fragmentManager.beginTransaction().add(R.id.content_frame, fragment).addToBackStack(null).commit();
                }
            });

            title_txt = (TextView) v.findViewById(R.id.title_txt);
            date_txt = (TextView) v.findViewById(R.id.date_txt);
            description_txt = (TextView) v.findViewById(R.id.description_txt);

        }
    }

    // Create new views (invoked by the layout manager)
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        // Create a new view.
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.testimony_feed_item_view, viewGroup, false);

        return new ViewHolder(v);
    }


    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {
        FeedItem item = mDataSet.get(position);

        viewHolder.title_txt.setText(item.Title);
        viewHolder.date_txt.setText(item.Date);
        viewHolder.description_txt.setText(item.Description);
    }


    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataSet.size();
    }
}
