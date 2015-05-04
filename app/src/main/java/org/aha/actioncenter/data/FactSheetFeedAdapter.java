package org.aha.actioncenter.data;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.aha.actioncenter.R;
import org.aha.actioncenter.models.FeedItem;
import org.aha.actioncenter.service.PdfDownloadAsyncTask;
import org.aha.actioncenter.utility.Utility;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;


/**
 * Created by markusmcgee on 4/16/15.
 */

/**
 * Provide views to RecyclerView with data from mDataSet.
 */
public class FactSheetFeedAdapter extends RecyclerView.Adapter<FactSheetFeedAdapter.ViewHolder> {
    private static final String TAG = "FactSheetFeedAdapter";

    private static List<FeedItem> mDataSet;
    private static Activity mActivity;
    private static Context mContext;

    public FactSheetFeedAdapter(Activity activity, List<FeedItem> dataSet) {
        mActivity = activity;
        mContext = activity.getApplicationContext();
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
                    Log.d(TAG, "Element " + getAdapterPosition() + " clicked.");

                    int position = getAdapterPosition();

                    FeedItem item = mDataSet.get(position);

                    if(Utility.getInstance().isNetworkAvailable(mActivity)) {
                        try {
                            new PdfDownloadAsyncTask(new URL(item.box_link_dir), mContext, mActivity).execute();
                        }
                        catch (MalformedURLException e) {
                            e.printStackTrace();
                        }
                    }


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
        Log.d(TAG, "Element " + position + " set.");

        FeedItem item = mDataSet.get(position);

        viewHolder.title_txt.setText(item.Title);
    }


    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataSet.size();
    }
}
