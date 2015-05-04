package org.aha.actioncenter.views;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.squareup.otto.Subscribe;

import org.aha.actioncenter.R;
import org.aha.actioncenter.events.ContactUsEvent;
import org.aha.actioncenter.models.OAMItem;
import org.aha.actioncenter.service.ContactUsAsyncTask;
import org.aha.actioncenter.utility.AHABusProvider;
import org.aha.actioncenter.utility.Utility;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

/**
 * Created by markusmcgee on 4/20/15.
 */
public class ContactUsFragment extends Fragment implements View.OnClickListener {

    private static final String TAG = "ContactUsFragment";
    protected TextView message_txt = null;
    protected Button send_btn = null;

    private Context mContext = null;

    @Override
    public void onClick(View view) {
        OAMItem item = Utility.getInstance(mContext).getLoginData();

        StringBuilder message = new StringBuilder();
        message.append(message_txt.getText());
        message.append(" Sent from ");
        message.append(item.first_name + " ");
        message.append(item.last_name + " ");
        message.append(item.org_name + " ");

        String urlString = mContext.getString(R.string.contact_us_url);
        try {
            urlString = urlString.replace("mMessage", URLEncoder.encode(message.toString(), "UTF-8"));
            urlString = urlString.replace("mFrom", item.email);

            URL url = new URL(urlString);
            new ContactUsAsyncTask(url, mContext, getActivity()).execute();

        }
        catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        catch (MalformedURLException e) {
            e.printStackTrace();
        }


    }

    @Subscribe
    public void subscribeContactUsEvent(ContactUsEvent event) {

        JSONObject json = null;
        try {
            json = event.getData();
            String status = json.getJSONObject("response").getString("status");
            if(status.equals("200")){
                new AlertDialog.Builder(getActivity())
                        .setTitle("Success")
                        .setMessage("Your message has been sent. Thank you for contacting the American Hospital Association")
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setCancelable(false)
                        .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // continue with delete
                                dialog.dismiss();
                                getFragmentManager().popBackStack();

                            }
                        })
                        .show();
            }
            else{
                new AlertDialog.Builder(getActivity())
                        .setTitle("Something Went Wrong")
                        .setMessage("Your message did not send. Please try again.")
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setCancelable(false)
                        .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // continue with delete
                                dialog.dismiss();
                            }
                        })
                        .show();
            }
        }
        catch (JSONException e) {
            e.printStackTrace();
        }

        Log.d(TAG, "debug");
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

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.contact_us_view, container, false);
        AHABusProvider.getInstance().register(this);

        mContext = getActivity().getApplicationContext();

        message_txt = (TextView) view.findViewById(R.id.message_txt);
        send_btn = (Button) view.findViewById(R.id.send_btn);

        send_btn.setOnClickListener(this);

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


}
