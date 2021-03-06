package org.aha.actioncenter.service;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Environment;

import org.aha.actioncenter.R;
import org.aha.actioncenter.events.PdfDataEvent;
import org.aha.actioncenter.utility.AHABusProvider;
import org.aha.actioncenter.utility.Utility;
import org.aha.actioncenter.BaseActivity;
import org.apache.commons.io.FilenameUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by markusmcgee on 4/15/15.
 */
public class PdfDownloadAsyncTask extends AsyncTask<Void, Void, String> {

    private static final String TAG = "PdfDownloadAsyncTask";
    private URL mUrl;
    private HttpURLConnection mConnection;
    private Context mContext;
    private Activity activity;

    private ProgressDialog progressDialog = null;

    public PdfDownloadAsyncTask(URL url, Context context, Activity activity) {
        this(url, context);
        this.activity = activity;
    }

    public PdfDownloadAsyncTask(URL url, Context context) {
        this.mContext = context;
        this.mUrl = url;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();

        if (!Utility.getInstance(mContext).isNetworkAvailable()) {
            cancel(true);
        }

        if (activity != null) {
            ((BaseActivity)activity).showProgressDialog("American Hospital Association", mContext.getString(R.string.downloading_pdf_message_txt));
        }
    }

    @Override
    protected void onCancelled() {
        ((BaseActivity)activity).closeProgressDialog();
        super.onCancelled();
    }

    @Override
    protected void onPostExecute(String fileLocation) {
        super.onPostExecute(fileLocation);

        PdfDataEvent event = new PdfDataEvent(PdfDataEvent.DOWNLOAD_DONE);
        event.setData(fileLocation);
        AHABusProvider.getInstance().post(event);

        ((BaseActivity)activity).closeProgressDialog();

    }

    @Override
    protected String doInBackground(Void... voids) {
        if (!Utility.getInstance(mContext).isNetworkAvailable()) {
            return "";
        }

        try {
            mConnection = (HttpURLConnection) mUrl.openConnection();

            String fileName = FilenameUtils.getName(mUrl.toString());

            String externalStorage = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).toString();

            File outputFile = new File(externalStorage, fileName);

            if (outputFile.exists()) {
                return fileName;
            }

            outputFile.createNewFile();


            FileOutputStream fos = new FileOutputStream(outputFile);
            InputStream is = mConnection.getInputStream();
            byte[] buffer = new byte[1024];
            int len1 = 0;
            while ((len1 = is.read(buffer)) != -1) {
                fos.write(buffer, 0, len1);
            }
            fos.flush();
            fos.close();
            is.close();

            String retVal = fileName;

            return retVal;
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        finally {
            mConnection.disconnect();
        }
        return "";
    }

}
