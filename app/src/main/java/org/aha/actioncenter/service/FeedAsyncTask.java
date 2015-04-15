package org.aha.actioncenter.service;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.aha.actioncenter.models.FeedItem;
import org.aha.actioncenter.views.FeedActivity;
import org.aha.actioncenter.views.FeedCallBack;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

/**
 * Created by markusmcgee on 4/15/15.
 */
public class FeedAsyncTask extends AsyncTask<Void, Void, String> {

    private static final String TAG = "FeedAsyncTask";
    private URL mUrl;
    private HttpURLConnection mConnection;
    private Context mContext;
    private FeedCallBack callback;

    public FeedAsyncTask(FeedCallBack callback, URL url, Context context) {
        this.callback = callback;
        this.mContext = context;
        this.mUrl = url;
    }

    @Override
    protected void onPostExecute(String feed) {
        super.onPostExecute(feed);
        JSONArray jsonArray;
        JSONObject json;
        String FILENAME = "feed.txt";


        try {
            json = new JSONObject(feed);
            jsonArray = json.getJSONArray("FEED_PAYLOAD");
            callback.feedCallBackDone(jsonArray);

            FileOutputStream fos = mContext.openFileOutput(FILENAME, Context.MODE_PRIVATE);
            fos.write(feed.getBytes());
            fos.close();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        catch (JSONException e) {
            e.printStackTrace();
        }

    }

    @Override
    protected String doInBackground(Void... voids) {
        if (!isNetworkAvailable()) {
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

    private boolean isNetworkAvailable() {
        ConnectivityManager cm = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();
        // if no network is available networkInfo will be null
        // otherwise check if we are connected
        if (networkInfo != null && networkInfo.isConnected()) {
            return true;
        }
        return false;
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
