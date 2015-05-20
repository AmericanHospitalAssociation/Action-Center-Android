package org.aha.actioncenter.views;

import android.app.Dialog;
import android.app.Fragment;
import android.content.Context;
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
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.aha.actioncenter.R;
import org.aha.actioncenter.models.CampaignSummaryItem;
import org.aha.actioncenter.models.CampaignUserItem;
import org.aha.actioncenter.models.OAMItem;
import org.aha.actioncenter.models.TakeActionGuidelinesItem;
import org.aha.actioncenter.service.TakeActionEmailAsyncTask;
import org.aha.actioncenter.utility.AHABusProvider;
import org.aha.actioncenter.utility.Utility;

import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.List;

/**
 * Created by markusmcgee on 4/17/15.
 */
public class TakeActionFragment extends Fragment {

    private static final String TAG = "TakeActionFragment";
    protected TextView subject_txt = null;
    protected EditText message_txt = null;
    protected Button send_btn = null;
    private Context mContext = null;
    private List<TakeActionGuidelinesItem> list = null;
    private List<CampaignUserItem> directoryList = null;

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

        mContext = getActivity().getApplicationContext();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.take_action_fragment_view, container, false);
        //OttoBus must be registered after inflate.inflate or app blows up.
        AHABusProvider.getInstance().register(this);

        getActivity().setTitle("Personalize the Message");

        list = Utility.getInstance(mContext).getTakeActionGuideline();
        directoryList = Utility.getInstance(mContext).getDirectoryData();

        String mMessages = list.get(0).message;
        String mSubject = list.get(0).subject;

        subject_txt = (TextView) view.findViewById(R.id.subject_txt);
        message_txt = (EditText) view.findViewById(R.id.message_txt);
        send_btn = (Button) view.findViewById(R.id.send_btn);

        subject_txt.setText(mSubject);
        message_txt.setText(mMessages);

        send_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getActivity().getApplicationContext(), "Take Action Send Click", Toast.LENGTH_SHORT).show();

                try {
                    String urlString = getString(R.string.take_action_url);

                    OAMItem oamItem = Utility.getInstance(mContext).getLoginData();

                    CampaignSummaryItem currentCampaignSummaryItem = Utility.getInstance(mContext).getCurrentCampaignSummaryItem();

                    urlString = urlString.replace("mUserId", oamItem.userid);
                    urlString = urlString.replace("mSignature", oamItem.first_name + " " + oamItem.last_name);
                    urlString = urlString.replace("mEmail", URLEncoder.encode(oamItem.email,"UTF-8"));
                    urlString = urlString.replace("mAddress", oamItem.address_line);
                    urlString = urlString.replace("mZipcode", oamItem.zip.substring(0, 5));
                    urlString = urlString.replace("mSubject", URLEncoder.encode(subject_txt.getText().toString(), "UTF-8"));
                    urlString = urlString.replace("mBody", URLEncoder.encode(message_txt.getText().toString(), "UTF-8"));
                    urlString = urlString.replace("mToken", oamItem.token);
                    urlString = urlString.replace("mMessageId", Utility.getInstance(mContext).getTakeActionBodyId());
                    urlString = urlString.replace("mCampaignId", currentCampaignSummaryItem.id);
                    urlString = urlString.replace("mTargets", Utility.getInstance(mContext).getTargetsForTakeAction());

                    URL url = new URL(urlString);

                    if (Utility.getInstance().isNetworkAvailable(getActivity())) {
                        new TakeActionEmailAsyncTask(url, mContext, getActivity()).execute();
                    }
                }
                catch (MalformedURLException e) {
                    e.printStackTrace();
                }
                catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        final Dialog dialog = new Dialog(getActivity());

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); //before
        dialog.setContentView(R.layout.guidelines_info_view);


        TextView recipient_txt = (TextView) dialog.findViewById(R.id.recipient_txt);
        TextView guidelines_txt = (TextView) dialog.findViewById(R.id.guidelines_txt);

        List<CampaignUserItem> directoryList = Utility.getInstance(mContext).getDirectoryData();

        if (list.size() > 0) {
            String s = list.get(0).guidelines;
            s = s.replace("\r\n", "<br/>");
            s = s.replace("<ul>", "");
            s = s.replace("<li>", "&#8226;\t");
            guidelines_txt.setText(Html.fromHtml(s));
            Log.d(TAG, s);
        }
        else {
            guidelines_txt.setText("Guidelines are missing. Please contact AHA for assistance.");
            Log.d(TAG, "Guidelines are missing. Please contact AHA for assistance.");
        }

        if (directoryList.size() > 0) {
            String recipientString = "";
            for (int i = 0; i < directoryList.size(); i++) {
                recipientString += "&#8226;\t" + directoryList.get(i).name + "<br/>";
            }
            if (recipientString.length() > 0)
                recipient_txt.setText(Html.fromHtml(recipientString));
            else
                recipient_txt.setText("Recipient list is missing.  Please contact AHA for assistance.");
        }
        else {
            recipient_txt.setText("Guidelines are missing. Please contact AHA for assistance.");
        }


        Button dialogButton = (Button) dialog.findViewById(R.id.btn_ok);
        // if button is clicked, close the custom dialog
        dialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();
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
