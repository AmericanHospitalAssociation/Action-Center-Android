package org.aha.actioncenter.service;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

import org.aha.actioncenter.events.CampaignDataEvent;
import org.aha.actioncenter.utility.AHABusProvider;
import org.aha.actioncenter.utility.Utility;
import org.json.JSONArray;
import org.json.JSONException;

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
public class CampaignAsyncTask extends AsyncTask<Void, Void, String> {
    private static final String TAG = "CampaignAsyncTask";
    private URL mUrl;
    private HttpURLConnection mConnection;
    private Context mContext;
    private Activity activity;

    private ProgressDialog progressDialog = null;

    public CampaignAsyncTask(URL url, Context context, Activity activity) {
        this(url, context);
        this.activity = activity;
    }

    public CampaignAsyncTask(URL url, Context context) {
        this.mContext = context;
        this.mUrl = url;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();

        if (!Utility.getInstance(mContext).isNetworkAvailable(activity)) {
            cancel(true);
        }

        if (!isCancelled()) {
            if (activity != null) {
                progressDialog = new ProgressDialog(activity);
                progressDialog.setTitle("American Hospital Association");
                progressDialog.setMessage("Loading Campaign Data...");
                progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                progressDialog.setCanceledOnTouchOutside(false);
                progressDialog.show();
            }
        }
    }

    @Override
    protected void onCancelled() {
        super.onCancelled();
        if (progressDialog != null && progressDialog.isShowing())
            progressDialog.dismiss();

        progressDialog = null;
    }


    @Override
    protected void onPostExecute(String feed) {
        super.onPostExecute(feed);

        JSONArray json = null;

        try {
            json = new JSONArray(feed);
        }
        catch (JSONException e) {
            e.printStackTrace();
        }


        if (json != null) {
            CampaignDataEvent event = new CampaignDataEvent(CampaignDataEvent.CAMPAIGN_DATA);
            event.setData(json);
            AHABusProvider.getInstance().post(event);
        }

        if (progressDialog != null && progressDialog.isShowing())
            progressDialog.dismiss();

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