package org.aha.actioncenter.data;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.aha.actioncenter.R;
import org.aha.actioncenter.models.NewsItem;
import org.aha.actioncenter.service.NewsLongDescriptionAsyncTask;
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
public class NewsFeedAdapter extends RecyclerView.Adapter<NewsFeedAdapter.ViewHolder> {
    private static final String TAG = "NewsFeedAdapter";

    private static List<NewsItem> mDataSet;
    private static Activity mActivity;

    public NewsFeedAdapter(Activity activity, List<NewsItem> dataSet) {
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
        protected TextView link_txt = null;


        public ViewHolder(View v) {
            super(v);
            // Define click listener for the ViewHolder's View.
            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    int position = getAdapterPosition();

                    NewsItem item = mDataSet.get(position);

                    try {
                        String urlString = mActivity.getString(R.string.news_detail_url);
                        urlString = urlString.replace("mNewsUrl", item.link);

                        URL url = new URL(urlString);
                        if(Utility.getInstance().isNetworkAvailable(mActivity)) {
                            new NewsLongDescriptionAsyncTask(url, mActivity.getApplicationContext(), mActivity, item).execute();
                        }
                    }
                    catch (MalformedURLException e) {
                        e.printStackTrace();
                    }

                }
            });

            title_txt = (TextView) v.findViewById(R.id.title_txt);
            description_txt = (TextView) v.findViewById(R.id.description_txt);
            date_txt = (TextView) v.findViewById(R.id.date_txt);
            link_txt = (TextView) v.findViewById(R.id.link_txt);

        }
    }


    // Create new views (invoked by the layout manager)
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        // Create a new view.
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.news_item_view, viewGroup, false);

        return new ViewHolder(v);
    }


    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {

        NewsItem item = mDataSet.get(position);

        viewHolder.title_txt.setText(item.title);
        viewHolder.description_txt.setText(item.description);
        viewHolder.date_txt.setText(item.date);
        viewHolder.link_txt.setText(item.link);
    }


    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataSet.size();
    }
}
