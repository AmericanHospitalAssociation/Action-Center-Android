package org.aha.actioncenter.service;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

import org.aha.actioncenter.MainActivity;
import org.aha.actioncenter.events.VoterVoiceDataEvent;
import org.aha.actioncenter.models.OAMItem;
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
 * Created by markusmcgee on 4/30/15.
 */
public class VoterVoiceCreateUserAsyncTask extends AsyncTask<Void, Void, String> {
    private static final String TAG = "VoterVoiceCreateUserAsyncTask";
    private URL mUrl;
    private HttpURLConnection mConnection;
    private Context mContext;
    private Activity activity;

    private ProgressDialog progressDialog = null;

    public VoterVoiceCreateUserAsyncTask(URL url, Context context, Activity activity) {
        this(url, context);
        this.activity = activity;
    }

    public VoterVoiceCreateUserAsyncTask(URL url, Context context) {
        this.mContext = context;
        this.mUrl = url;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();

        if (!Utility.getInstance(mContext).isNetworkAvailable()) {
            cancel(true);
        }

        if (!isCancelled()) {
            if (activity != null) {
                ((MainActivity)activity).showProgressDialog("American Hospital Association", "Create Directory User ...");
            }
        }
    }

    @Override
    protected void onCancelled() {
        ((MainActivity)activity).closeProgressDialog();
        super.onCancelled();
    }


    @Override
    protected void onPostExecute(String feed) {
        super.onPostExecute(feed);

        JSONObject json = null;

        try {
            json = new JSONObject(feed);
            OAMItem oamItem = Utility.getInstance(mContext).getLoginData();
            oamItem.token = json.getJSONObject("response").getJSONObject("body").getString("userToken");
            oamItem.userid = json.getJSONObject("response").getJSONObject("body").getString("userId");
            Utility.getInstance(mContext).saveLoginData("login", oamItem);
        }
        catch (JSONException e) {
            e.printStackTrace();
        }

        VoterVoiceDataEvent event = new VoterVoiceDataEvent(VoterVoiceDataEvent.VOTER_VOICE_CREATE_DATA);
        AHABusProvider.getInstance().post(event);

        ((MainActivity)activity).closeProgressDialog();

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

