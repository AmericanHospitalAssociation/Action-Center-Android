package org.aha.actioncenter.service;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;

import org.aha.actioncenter.events.FeedDataEvent;
import org.aha.actioncenter.utility.AHABusProvider;
import org.aha.actioncenter.utility.Utility;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by markusmcgee on 4/15/15.
 */
public class FeedAsyncTask extends AsyncTask<Void, Void, String> {

    private static final String TAG = "FeedAsyncTask";
    private URL mUrl;
    private HttpURLConnection mConnection;
    private Context mContext;

    public FeedAsyncTask(URL url, Context context) {
        this.mContext = context;
        this.mUrl = url;
    }

    @Override
    protected void onPostExecute(String feed) {
        super.onPostExecute(feed);

        JSONObject json = null;

        try {
            json = new JSONObject(feed);
        }
        catch (JSONException e) {
            e.printStackTrace();
        }

        FeedDataEvent event = new FeedDataEvent(FeedDataEvent.FEED_DATA);
        event.setData(json);
        AHABusProvider.getInstance().post(event);

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
