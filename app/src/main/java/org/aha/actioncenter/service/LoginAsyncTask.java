package org.aha.actioncenter.service;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

import org.aha.actioncenter.events.LoginEvent;
import org.aha.actioncenter.utility.AHABusProvider;
import org.aha.actioncenter.utility.Utility;
import org.aha.actioncenter.BaseActivity;
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
 * Created by markusmcgee on 4/21/15.
 */
public class LoginAsyncTask extends AsyncTask<Void, Void, String> {
    private static final String TAG = "LoginAsyncTask";
    private URL mUrl;
    private HttpURLConnection mConnection;
    private Context mContext;
    private Activity activity;

    private ProgressDialog progressDialog = null;

    public LoginAsyncTask(URL url, Context context, Activity activity) {
        this(url, context);
        this.activity = activity;
    }

    public LoginAsyncTask(URL url, Context context) {
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
                ((BaseActivity)activity).showProgressDialog("American Hospital Association", "Login Event Data...");
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
        }
        catch (JSONException e) {
            e.printStackTrace();
        }

        Utility.getInstance(mContext).saveLoginData("login", json);

        LoginEvent event = new LoginEvent(LoginEvent.LOGIN_DATA);
        AHABusProvider.getInstance().post(event);

        ((BaseActivity)activity).closeProgressDialog();
    }

    @Override
    protected String doInBackground(Void... voids) {
        if(!Utility.getInstance(mContext).isNetworkAvailable()) {
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
        BufferedReader r = new BufferedReader(new InputStreamReader(in),1000);
        for (String line = r.readLine(); line != null; line =r.readLine()){
            sb.append(line);
        }
        in.close();
        return sb.toString();
    }

}
