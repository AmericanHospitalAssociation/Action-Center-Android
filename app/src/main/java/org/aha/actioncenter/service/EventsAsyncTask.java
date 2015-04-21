package org.aha.actioncenter.service;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import org.aha.actioncenter.utility.Utility;

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
public class EventsAsyncTask extends AsyncTask<Void, Void, String> {
    private static final String TAG = "EventsAsyncTask";
    private URL mUrl;
    private HttpURLConnection mConnection;
    private Context mContext;

    public EventsAsyncTask(URL url, Context context) {
        this.mContext = context;
        this.mUrl = url;
    }

    @Override
    protected void onPostExecute(String events) {
        super.onPostExecute(events);

        Log.d(TAG, events);

        String FILENAME = "events.txt";
        try {
            FileOutputStream fos = mContext.openFileOutput(FILENAME, Context.MODE_PRIVATE);
            fos.write(events.getBytes());
            fos.close();
        }
        catch (IOException e) {
            e.printStackTrace();
        }

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
