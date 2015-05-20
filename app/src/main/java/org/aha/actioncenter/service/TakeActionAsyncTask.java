package org.aha.actioncenter.service;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;

import org.aha.actioncenter.BaseActivity;
import org.aha.actioncenter.R;
import org.aha.actioncenter.events.TakeActionEvent;
import org.aha.actioncenter.utility.AHABusProvider;
import org.aha.actioncenter.utility.Utility;
import org.json.JSONArray;
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
public class TakeActionAsyncTask extends AsyncTask<Void, Void, String> {

    private static final String TAG = "TakeActionAsyncTask";
    private URL mUrl;
    private HttpURLConnection mConnection;
    private Context mContext;
    private Activity activity;

    public TakeActionAsyncTask(URL url, Context context, Activity activity) {
        this(url, context);
        this.activity = activity;
    }

    public TakeActionAsyncTask(URL url, Context context) {
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
                ((BaseActivity) activity).showProgressDialog("American Hospital Association", mContext.getString(R.string.loading_take_action_message_txt));
            }
        }
    }

    @Override
    protected void onCancelled() {
        ((BaseActivity) activity).closeProgressDialog();
        super.onCancelled();
    }

    @Override
    protected void onPostExecute(String feed) {
        super.onPostExecute(feed);

        JSONObject json = null;
        JSONArray aMessages = null;
        JSONArray aPreselectedAnswers = null;

        try {
            json = new JSONObject(feed);
            aMessages = json.getJSONObject("response").getJSONArray("body").getJSONObject(0).getJSONArray("messages");
            Utility.getInstance(mContext).saveTakeActionGuideline(aMessages);
            Utility.getInstance(mContext).saveTakeActionBodyId(json.getJSONObject("response").getJSONArray("body").getJSONObject(0).get("id").toString());

            aPreselectedAnswers = json.getJSONObject("response").getJSONArray("body").getJSONObject(0).getJSONArray("preSelectedAnswers");
            Utility.getInstance(mContext).savePreselectedAnswers(aPreselectedAnswers);


        }
        catch (JSONException e) {
            e.printStackTrace();
        }

        TakeActionEvent event = new TakeActionEvent(TakeActionEvent.TAKE_ACTION_DATA);
        AHABusProvider.getInstance().post(event);

        ((BaseActivity) activity).closeProgressDialog();
    }

    @Override
    protected String doInBackground(Void... voids) {
        try {
            if (!isCancelled()) {
                mConnection = (HttpURLConnection) mUrl.openConnection();
                InputStream in = new BufferedInputStream(mConnection.getInputStream());
                String output = readStream(in);
                return output;
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        finally {
            if (mConnection != null)
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
