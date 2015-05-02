package org.aha.actioncenter.views;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.TableLayout;

import org.aha.actioncenter.R;
import org.aha.actioncenter.models.FeedItem;
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
    private AlertDialog alertDialog;

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

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        inflater = getActivity().getMenuInflater();
        inflater.inflate(R.menu.detail_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.close_detail_action) {
            getFragmentManager().popBackStack();
        }
        return super.onOptionsItemSelected(item);
    }


}