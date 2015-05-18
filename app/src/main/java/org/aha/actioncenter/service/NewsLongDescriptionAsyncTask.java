package org.aha.actioncenter.service;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;

import org.aha.actioncenter.BaseActivity;
import org.aha.actioncenter.R;
import org.aha.actioncenter.events.NewsDataEvent;
import org.aha.actioncenter.models.NewsItem;
import org.aha.actioncenter.utility.AHABusProvider;
import org.aha.actioncenter.utility.Utility;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by markusmcgee on 4/15/15.
 */
public class NewsLongDescriptionAsyncTask extends AsyncTask<Void, Void, String> {
    private static final String TAG = "NewsLongDescriptionAsyncTask";
    private URL mUrl;
    private HttpURLConnection mConnection;
    private Context mContext;
    private Activity activity;
    private NewsItem item;

    public NewsLongDescriptionAsyncTask(URL url, Context context, Activity activity) {
        this(url, context);
        this.activity = activity;
    }

    public NewsLongDescriptionAsyncTask(URL url, Context context) {
        this.mContext = context;
        this.mUrl = url;
    }

    public NewsLongDescriptionAsyncTask(URL url, Context context, Activity activity, NewsItem item) {
        this(url, context, activity);
        this.item = item;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();

        if (!Utility.getInstance(mContext).isNetworkAvailable()) {
            cancel(true);
        }

        if (!isCancelled()) {
            if (activity != null) {
                ((BaseActivity)activity).showProgressDialog("American Hospital Association", mContext.getString(R.string.loading_message_txt));
            }
        }
    }

    @Override
    protected void onCancelled() {
        ((BaseActivity)activity).closeProgressDialog();
        super.onCancelled();
    }


    @Override
    protected void onPostExecute(String feed) {
        super.onPostExecute(feed);

        JSONObject json = null;

        try {
            json = new JSONObject(feed);

            if (json != null) {
                NewsDataEvent event = new NewsDataEvent(NewsDataEvent.NEWS_LONG_DESCRIPTION_DATA);
                item.long_description = json.getString("response");
                event.setNewsItem(item);
                AHABusProvider.getInstance().post(event);
            }
        }
        catch (JSONException e) {
            e.printStackTrace();
        }

        ((BaseActivity)activity).closeProgressDialog();

    }

    @Override
    protected String doInBackground(Void... voids) {
        if (!Utility.getInstance(mContext).isNetworkAvailable()) {
            return "";
        }

        try {
            mConnection = (HttpURLConnection) mUrl.openConnection();
            InputStream in = new BufferedInputStream(mConnection.getInputStream());
            String output = readStream(in);
            return output;
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        finally {
            mConnection.disconnect();
        }
        return "";
    }

    public static String readStream(InputStream in) throws IOException {
        StringBuilder sb = new StringBuilder();
        BufferedReader r = new BufferedReader(new InputStreamReader(in), 1000);
        for (String line = r.readLine(); line != null; line = r.readLine()) {
            sb.append(line);
        }
        in.close();
        return sb.toString();
    }

}
