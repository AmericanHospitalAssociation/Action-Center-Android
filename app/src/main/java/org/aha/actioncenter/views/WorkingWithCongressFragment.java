package org.aha.actioncenter.views;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.TableLayout;

import com.squareup.otto.Subscribe;

import org.aha.actioncenter.R;
import org.aha.actioncenter.events.FeedDataEvent;
import org.aha.actioncenter.events.PdfDataEvent;
import org.aha.actioncenter.models.FeedItem;
import org.aha.actioncenter.service.FeedAsyncTask;
import org.aha.actioncenter.service.PdfDownloadAsyncTask;
import org.aha.actioncenter.utility.AHABusProvider;
import org.aha.actioncenter.utility.Utility;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

/**
 * Created by markusmcgee on 4/17/15.
 */
public class WorkingWithCongressFragment extends Fragment {

    private static final String TAG = "WorkingWithCongressFragment";
    private WebView mWebView;
    private TableLayout feedTable;
    private List<FeedItem> list;
    private Context mContext = null;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        //OttoBus must be registered after inflate.inflate or app blows up.
        AHABusProvider.getInstance().register(this);

        mContext = getActivity().getApplicationContext();

        View view = inflater.inflate(R.layout.webview_view, container, false);


        mWebView = (WebView) view.findViewById(R.id.webview);

        list = Utility.getInstance(mContext).getFeedData(Utility.getInstance().WORKING_WITH_CONGRESS);


        try {
            new PdfDownloadAsyncTask(new URL(list.get(0).ResourceURI), mContext, getActivity()).execute();
        }
        catch (MalformedURLException e) {
            e.printStackTrace();
        }

/*

        String mUrl = "http://docs.google.com/gview?embedded=true&url=" + list.get(0).ResourceURI;


        PackageManager packageManager = getActivity().getPackageManager();
        Intent intent = new Intent(Intent.ACTION_VIEW)
                .setType("application/pdf");
        List<ResolveInfo> intentList = packageManager.queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);

        if (intentList.size() > 0) {
            // Happy days a PDF reader exists
            intent = new Intent(Intent.ACTION_VIEW)
                    .setData(Uri.parse(list.get(0).ResourceURI));
            startActivity(intent);
        } else {
            // No PDF reader, ask the user to download one first
            // or just open it in their browser like this
            //intent = new Intent(Intent.ACTION_VIEW)
            //        .setData(Uri.parse(mUrl));
            //startActivity(intent);

            mWebView.getSettings().setSupportZoom(true);
            mWebView.getSettings().setAllowFileAccess(true);
            mWebView.getSettings().setAppCacheEnabled(true);
            mWebView.getSettings().setBuiltInZoomControls(true);
            mWebView.getSettings().setJavaScriptEnabled(true);
            mWebView.loadUrl(mUrl);


        }

*/

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        AHABusProvider.getInstance().register(this);

        list = Utility.getInstance(mContext).getFeedData(Utility.getInstance().WORKING_WITH_CONGRESS);
    }

    @Override
    public void onPause() {
        super.onPause();
        AHABusProvider.getInstance().unregister(this);
    }

    private void refreshFeedData() {
        try {
            URL url = new URL(getResources().getString(R.string.feed_url));
            FeedAsyncTask feedAsync = new FeedAsyncTask(url, mContext);
            feedAsync.execute();
        }
        catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    //Subscribe to Feed data event.  If data comes in update view.
    @Subscribe
    public void subscribeOnFeedDataEvent(FeedDataEvent event) {
        // specify an adapter (see also next example)
        if (Utility.getInstance(mContext).isFeedDataLoaded()) {
            list = Utility.getInstance(mContext).getFeedData(Utility.getInstance().WORKING_WITH_CONGRESS);
        }
    }

    public void subscribeOnPDFDownload(PdfDataEvent event){
        Log.d(TAG, "Loaded");
    }
}
