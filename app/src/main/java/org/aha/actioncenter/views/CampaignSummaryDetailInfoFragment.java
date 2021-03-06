package org.aha.actioncenter.views;

import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.squareup.otto.Subscribe;

import org.aha.actioncenter.MainActivity;
import org.aha.actioncenter.R;
import org.aha.actioncenter.events.TakeActionEvent;
import org.aha.actioncenter.models.CampaignSummaryItem;
import org.aha.actioncenter.models.OAMItem;
import org.aha.actioncenter.service.TakeActionAsyncTask;
import org.aha.actioncenter.utility.AHABusProvider;
import org.aha.actioncenter.utility.Utility;

import java.lang.reflect.Type;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by markusmcgee on 4/17/15.
 */
public class CampaignSummaryDetailInfoFragment extends Fragment {

    private static String TAG = "CampaignSummaryDetailInfoFragment";

    protected TextView title_txt = null;
    protected TextView long_description_txt = null;
    protected TextView resource_uri_txt = null;
    protected Button take_action_btn = null;
    private static CampaignSummaryItem item = null;
    private boolean mShowUserGuideliness = false;

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

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.campaign_summary_detail_view, container, false);
        //OttoBus must be registered after inflate.inflate or app blows up.
        AHABusProvider.getInstance().register(this);

        Type campaignSummaryItemType = new TypeToken<CampaignSummaryItem>() {
        }.getType();
        item = new Gson().fromJson(getArguments().getString("item"), campaignSummaryItemType);
        Utility.getInstance(getActivity().getApplicationContext()).saveCurrentCampaignSummaryItem(item);

        title_txt = (TextView) view.findViewById(R.id.title_txt);
        long_description_txt = (TextView) view.findViewById(R.id.long_description_txt);
        resource_uri_txt = (TextView) view.findViewById(R.id.resource_uri_txt);
        take_action_btn = (Button) view.findViewById(R.id.take_action_btn);

        title_txt.setText(item.headline);
        long_description_txt.setText(Html.fromHtml(item.alert));
        //resource_uri_txt = (TextView) view.findViewById(R.id.resource_uri_txt);

        take_action_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                OAMItem oamItem = Utility.getInstance(getActivity().getApplicationContext()).getLoginData();
                if (oamItem.prefix == null || oamItem.phone == null) {

                    new AlertDialog.Builder(getActivity()).setTitle("Additional Info Needed").setMessage("To enable matching you to your legislators, additional info is needed. Would you like to enter the needed info?").setNegativeButton(android.R.string.no, null).setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface arg0, int arg1) {

                            Fragment fragment = new MissingInfoFragment();
                            ((MainActivity) getActivity()).addToFragmentBackStack(fragment, "update-user", "Update User Information");


                        }
                    }).create().show();
                    //return;
                }

                try {
                    String urlString = getResources().getString(R.string.vv_targeted_message_url);
                    urlString = urlString.replace("mCampaignId", item.id);
                    URL url = new URL(urlString);
                    if (Utility.getInstance().isNetworkAvailable(getActivity())) {
                        new TakeActionAsyncTask(url, getActivity().getApplicationContext(), getActivity()).execute();
                    }
                }
                catch (MalformedURLException e) {
                    e.printStackTrace();
                }
            }
        });
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        AHABusProvider.getInstance().register(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        AHABusProvider.getInstance().unregister(this);
    }


    @Subscribe
    public void subscribeTakeActionEvent(TakeActionEvent event) {
        FragmentManager fragmentManager = getFragmentManager();
        TakeActionFragment fragment = new TakeActionFragment();
        fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).addToBackStack("take-action").commit();


    }
}
