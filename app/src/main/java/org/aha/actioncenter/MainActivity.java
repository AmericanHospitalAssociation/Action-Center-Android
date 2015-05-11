package org.aha.actioncenter;

import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.telephony.PhoneNumberUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import com.squareup.otto.Subscribe;

import org.aha.actioncenter.data.AHAExpandableListAdapter;
import org.aha.actioncenter.events.CampaignDataEvent;
import org.aha.actioncenter.events.FeedDataEvent;
import org.aha.actioncenter.events.PdfDataEvent;
import org.aha.actioncenter.events.VoterVoiceDataEvent;
import org.aha.actioncenter.models.FeedItem;
import org.aha.actioncenter.models.NavigationItem;
import org.aha.actioncenter.models.OAMItem;
import org.aha.actioncenter.service.CampaignSummaryAsyncTask;
import org.aha.actioncenter.service.PdfDownloadAsyncTask;
import org.aha.actioncenter.service.VoterVoiceCreateUserAsyncTask;
import org.aha.actioncenter.service.VoterVoiceMatchesCampaignAsyncTask;
import org.aha.actioncenter.utility.AHABusProvider;
import org.aha.actioncenter.utility.Utility;
import org.aha.actioncenter.views.ActionAlertListFragment;
import org.aha.actioncenter.views.AdditionalInfoListFragment;
import org.aha.actioncenter.views.AdvisoryListFragment;
import org.aha.actioncenter.views.CampaignSummaryListFragment;
import org.aha.actioncenter.views.ContactUsFragment;
import org.aha.actioncenter.views.ContactYourLegislatorsListFragment;
import org.aha.actioncenter.views.DirectoryListFragment;
import org.aha.actioncenter.views.EventsListFragment;
import org.aha.actioncenter.views.FactSheetListFragment;
import org.aha.actioncenter.views.HomeFragment;
import org.aha.actioncenter.views.LetterListFragment;
import org.aha.actioncenter.views.MissingInfoFragment;
import org.aha.actioncenter.views.NewsListFragment;
import org.aha.actioncenter.views.SpecialBulletinListFragment;
import org.aha.actioncenter.views.TestimonyListFragment;
import org.aha.actioncenter.views.TwitterFeedListFragment;
import org.aha.actioncenter.views.WorkingWithCongressFragment;
import org.json.JSONArray;
import org.json.JSONException;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends BaseActivity implements ExpandableListView.OnGroupClickListener, ExpandableListView.OnChildClickListener, View.OnClickListener, FragmentManager.OnBackStackChangedListener {

    private static final String TAG = "MainActivity";
    private DrawerLayout mDrawerLayout;
    private ExpandableListView mDrawerList;
    private ActionBarDrawerToggle mDrawerToggle;
    private CharSequence mDrawerTitle;
    private CharSequence mTitle;

    private Context mContext = null;
    private ArrayList<NavigationItem> navigationItemArrayList = null;
    private AHAExpandableListAdapter navigationAdapter = null;


    private View mClickedChild = null;
    private NavigationItem currentNavigationItem = null;

    // Note: Your consumer key and secret should be obfuscated in your source code before shipping.

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mContext = getApplicationContext();

        //Register bus provider to listen to events.
        AHABusProvider.getInstance().register(this);

        if (!Utility.getInstance().contentLoaded())
            ((AHAActionCenterApplication) getApplicationContext()).pullAdditionalData(this);

        setContentView(R.layout.activity_main);

        ImageView imageView = (ImageView) findViewById(android.R.id.home);
        imageView.setPadding(10, 0, 0, 0);

        mTitle = mDrawerTitle = getTitle();

        NavigationItem navigationItem = null;

        Reader reader = null;
        InputStream stream = getResources().openRawResource(R.raw.navigation);
        reader = new BufferedReader(new InputStreamReader(stream), 8092);

        // parse json
        JsonParser parser = new JsonParser();
        JsonObject jNavigation = (JsonObject) parser.parse(reader);

        Type listType = new TypeToken<ArrayList<NavigationItem>>() {
        }.getType();
        navigationItemArrayList = new Gson().fromJson(jNavigation.getAsJsonArray("topnav"), listType);

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ExpandableListView) findViewById(R.id.navigation_view);
        mDrawerList.setGroupIndicator(null);

        View view = getLayoutInflater().inflate(R.layout.navigation_logout_footer, null);

        LinearLayout footerLayout = (LinearLayout) view.findViewById(R.id.logout_view);

        Button button = (Button) footerLayout.findViewById(R.id.logout_btn);
        button.setOnClickListener(this);

        // Add the footer before the setAdapter() method
        mDrawerList.addFooterView(footerLayout);

        mDrawerList.setOnGroupClickListener(this);
        mDrawerList.setOnChildClickListener(this);

        // Set the adapter for the list view
        navigationAdapter = new AHAExpandableListAdapter(this, navigationItemArrayList);
        mDrawerList.setAdapter(navigationAdapter);
        // Set the list's click listener

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerToggle = new ActionBarDrawerToggle(this,                  /* host Activity */
                mDrawerLayout,         /* DrawerLayout object */
                R.string.drawer_open,  /* "open drawer" description */
                R.string.drawer_close  /* "close drawer" description */) {

            /** Called when a drawer has settled in a completely closed state. */
            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
                getActionBar().setTitle(mTitle);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }

            /** Called when a drawer has settled in a completely open state. */
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                getActionBar().setTitle(mDrawerTitle);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }
        };

        // Set the drawer toggle as the DrawerListener
        mDrawerLayout.setDrawerListener(mDrawerToggle);

        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setHomeButtonEnabled(true);

        FragmentManager fragmentManager = getFragmentManager();
        Fragment fragment = new HomeFragment();
        fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).addToBackStack(Utility.getInstance().HOME).commit();

        setTitle("WELCOME");


    }

    /* Called whenever we call invalidateOptionsMenu() */
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        // If the nav drawer is open, hide action items related to the content view
        boolean drawerOpen = mDrawerLayout.isDrawerOpen(mDrawerList);
        return super.onPrepareOptionsMenu(menu);
    }

    //Subscribe to Feed data event.  This should probably listen only once then unregister it self.
    //MainActivity should only be shown once?
    @Subscribe
    public void subscribeOnFeedDataEvent(FeedDataEvent event) {
        try {
            JSONArray jArray = null;
            jArray = (JSONArray) event.getData().getJSONArray("FEED_PAYLOAD");
            Utility.getInstance(mContext).parseFeedData(jArray);
        }
        catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onResume() {
        AHABusProvider.getInstance().register(this);
        super.onResume();
    }

    @Override
    protected void onPause() {
        AHABusProvider.getInstance().unregister(this);
        if (progressDialog != null && progressDialog.isShowing())
            progressDialog.dismiss();
        super.onPause();
    }

    /**
     * Swaps fragments in the main content view
     */

    //TODO: Disable multiple clicks, data request.
    private boolean onePassClick = false;

    public void selectItem(){
        selectItem(currentNavigationItem);
    }

    public void selectItem(NavigationItem item) {
        Log.d(TAG, "Navigation item selected.");

        Fragment fragment = null;
        Bundle args = new Bundle();
        //fragment.setArguments(args);

        if (item.id.equals(Utility.getInstance().HOME))
            fragment = new HomeFragment();
        if (item.id.equals(Utility.getInstance().ACTION_ALERT))
            fragment = new ActionAlertListFragment();
        if (item.id.equals(Utility.getInstance().FACT_SHEET))
            fragment = new FactSheetListFragment();
        if (item.id.equals(Utility.getInstance().BULLETIN))
            fragment = new SpecialBulletinListFragment();
        if (item.id.equals(Utility.getInstance().ADVISORY))
            fragment = new AdvisoryListFragment();
        if (item.id.equals(Utility.getInstance().LETTER))
            fragment = new LetterListFragment();
        if (item.id.equals(Utility.getInstance().TESTIMONY))
            fragment = new TestimonyListFragment();
        if (item.id.equals(Utility.getInstance().ADDITIONAL_INFO))
            fragment = new AdditionalInfoListFragment();
        if (item.id.equals(Utility.getInstance().WORKING_WITH_CONGRESS)) {
            //fragment = new WorkingWithCongressFragment();
            List<FeedItem> list = Utility.getInstance(mContext).getFeedData(Utility.getInstance().WORKING_WITH_CONGRESS);

            if(Utility.getInstance().isNetworkAvailable(this)) {
                try {
                    new PdfDownloadAsyncTask(new URL(list.get(0).ResourceURI.isEmpty() ? list.get(0).box_link_dir : list.get(0).ResourceURI), getApplicationContext(), this).execute();
                }
                catch (MalformedURLException e) {
                    e.printStackTrace();
                }
            }

        }
        if (item.id.equals(Utility.getInstance().EVENTS))
            fragment = new EventsListFragment();
        if (item.id.equals(Utility.getInstance().NEWS))
            fragment = new NewsListFragment();
        if (item.id.equals(Utility.getInstance().CONTACT_US))
            fragment = new ContactUsFragment();
        if (item.id.equals(Utility.getInstance().CONGRESSIONAL_CALENDAR)) {
            FeedItem feedItem = Utility.getInstance(mContext).getCongressionalCalendar();
            if (Utility.getInstance().isNetworkAvailable(this)) {
                try {
                    new PdfDownloadAsyncTask(new URL(feedItem.ResourceURI.isEmpty() ? feedItem.box_link_dir : feedItem.ResourceURI), getApplicationContext(), this).execute();
                }
                catch (MalformedURLException e) {
                    e.printStackTrace();
                }
            }
        }
        if (item.id.equals(Utility.getInstance().DIRECTORY)) {

            OAMItem oamItem = Utility.getInstance(mContext).getLoginData();

            if (oamItem.prefix == null || oamItem.phone == null) {

                new AlertDialog.Builder(this).setTitle("Additional Info Needed").setMessage("To enable matching you to your legislators, additional info is needed. Would you like to enter the needed info?").setNegativeButton(android.R.string.no, null).setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface arg0, int arg1) {

                        Fragment fragment = new MissingInfoFragment();
                        addToFragmentBackStack(fragment, "update-user", "Update User Information");

                        Log.d(TAG, "debug");

                    }
                }).create().show();

            }
            else {
                try {

                    String phone = PhoneNumberUtils.stripSeparators((oamItem.phone != null ? oamItem.phone : ""));

                    String urlString = getResources().getString(R.string.vv_create_user_url);
                    urlString = urlString.replace("mOrg", URLEncoder.encode((oamItem.org_name != null ? oamItem.org_name : ""), "UTF-8"));
                    urlString = urlString.replace("mEmail", URLEncoder.encode((oamItem.email != null ? oamItem.email : ""), "UTF-8"));
                    urlString = urlString.replace("mFirstName", URLEncoder.encode((oamItem.first_name != null ? oamItem.first_name : ""), "UTF-8"));
                    urlString = urlString.replace("mLastName", URLEncoder.encode((oamItem.last_name != null ? oamItem.last_name : ""), "UTF-8"));
                    urlString = urlString.replace("mAddress", URLEncoder.encode((oamItem.address_line != null ? oamItem.address_line : ""), "UTF-8"));
                    urlString = urlString.replace("mZipcode", URLEncoder.encode((oamItem.zip != null ? oamItem.zip : ""), "UTF-8"));
                    urlString = urlString.replace("mCountry", URLEncoder.encode((oamItem.country != null ? oamItem.country : "us"), "UTF-8"));
                    urlString = urlString.replace("mCity", URLEncoder.encode((oamItem.city != null ? oamItem.city : ""), "UTF-8"));
                    urlString = urlString.replace("mPhone", phone);
                    urlString = urlString.replace("mPrefix", URLEncoder.encode((oamItem.prefix != null ? oamItem.prefix : ""), "UTF-8"));

                    Log.d(TAG, urlString);
                    URL url = new URL(urlString);
                    if (Utility.getInstance().isNetworkAvailable(this)) {
                        new VoterVoiceCreateUserAsyncTask(url, getApplicationContext(), this).execute();
                    }
                }
                catch (MalformedURLException e) {
                    e.printStackTrace();
                }
                catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }
        }
        if (item.id.equals(Utility.getInstance().CONTACT_YOUR_LEGISLATORS)) {
            try {

                String urlString = getResources().getString(R.string.vv_campaign_summary_url);
                URL url = new URL(urlString);
                if (Utility.getInstance().isNetworkAvailable(this)) {
                    new CampaignSummaryAsyncTask(url, mContext, this).execute();
                }
            }
            catch (MalformedURLException e) {
                e.printStackTrace();
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (item.id.equals(Utility.getInstance().TWITTER_FEEDS)) {
            if (Utility.getInstance().isNetworkAvailable(this)) {
                args.putString(item.id, item.user);
                fragment = new TwitterFeedListFragment();
                fragment.setArguments(args);
            }
            else {
                return;
            }
        }

        addToFragmentBackStack(fragment, item);
    }

    public void addToFragmentBackStack(Fragment fragment, String navigationId, String navigationName) {
        addToAppBackStack(fragment, navigationId, navigationName);
    }

    public void addToFragmentBackStack(Fragment fragment, NavigationItem item) {
        addToAppBackStack(fragment, item.id, item.name);
    }

    public void addToAppBackStack(Fragment fragment, String navigationId, String navigationName) {
        // Insert the fragment by replacing any existing fragment
        FragmentManager fragmentManager = getFragmentManager();

        if (fragment != null) {

            fragmentManager.addOnBackStackChangedListener(this);
            //fragmentManager.beginTransaction().add(R.id.content_frame, fragment).addToBackStack(item.id).commit();
            fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).addToBackStack(navigationId).commit();

            // Highlight the selected item, update the title, and close the drawer
            setTitle(navigationName);
            mDrawerLayout.closeDrawer(mDrawerList);
        }
    }

    @Override
    public void setTitle(CharSequence title) {
        mTitle = title;
        getActionBar().setTitle(mTitle);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Pass the event to ActionBarDrawerToggle, if it returns
        // true, then it has handled the app icon touch event
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        // Handle your other action bar items...

        return super.onOptionsItemSelected(item);
    }


    @Override
    public boolean onGroupClick(ExpandableListView expandableListView, View view, int groupPosition, long id) {
        Log.d(TAG, "onGroupClick");
        NavigationItem navigationItem = (NavigationItem) navigationAdapter.getGroup(groupPosition);
        currentNavigationItem = navigationItem;
        if (navigationItem.id.equals(Utility.getInstance().HOME) ||
                navigationItem.id.equals(Utility.getInstance().EVENTS) ||
                navigationItem.id.equals(Utility.getInstance().NEWS) ||
                navigationItem.id.equals(Utility.getInstance().CONTACT_US)) {
            selectItem(navigationItem);

        }
        return false;
    }


    @Override
    public boolean onChildClick(ExpandableListView expandableListView, View view, int groupPosition, int childPosition, long id) {
        Log.d(TAG, "onChildClick");
        NavigationItem navigationItem = (NavigationItem) navigationAdapter.getChild(groupPosition, childPosition);
        currentNavigationItem = navigationItem;
        selectItem(navigationItem);

        if (mClickedChild != null) {
            mClickedChild.setBackgroundColor(Color.TRANSPARENT);
            mClickedChild.invalidate();
        }
        mClickedChild = view;
        view.setBackgroundColor(getResources().getColor(R.color.aha_darker_grey));


        return false;
    }

    @Override
    public void onClick(View view) {
        Intent intent;

        Utility.getInstance(mContext).logoutCurrentUser();

        intent = new Intent(this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    @Override
    public void onBackStackChanged() {

        Fragment fragment = getFragment();

        if (fragment instanceof HomeFragment)
            setTitle("Welcome");
        if (fragment instanceof ActionAlertListFragment)
            setTitle("Action Alert");
        if (fragment instanceof FactSheetListFragment)
            setTitle("Fact Sheet");
        if (fragment instanceof SpecialBulletinListFragment)
            setTitle("Special Bulletins");
        if (fragment instanceof AdvisoryListFragment)
            setTitle("Advisory");
        if (fragment instanceof LetterListFragment)
            setTitle("Letters");
        if (fragment instanceof TestimonyListFragment)
            setTitle("Testimony");
        if (fragment instanceof AdditionalInfoListFragment)
            setTitle("Additional Info");
        if (fragment instanceof WorkingWithCongressFragment)
            setTitle("Working with Congress");
        if (fragment instanceof EventsListFragment)
            setTitle("Events");
        if (fragment instanceof TwitterFeedListFragment)
            setTitle("Twitter Feed");

        Log.d(TAG, "onBackStackChanged");
    }

    /**
     * Returns the currently displayed fragment.
     *
     * @return Fragment or null.
     */
    private Fragment getFragment() {
        Fragment fragment = getFragmentManager().findFragmentById(R.id.content_frame);
        return fragment;
    }

    @Override
    public void onBackPressed() {
        int count = getFragmentManager().getBackStackEntryCount();
        mDrawerLayout.closeDrawers();
        if (count <= 1) {
            //super.onBackPressed();
            new AlertDialog.Builder(this).setTitle("Exit app?").setMessage("Are you sure you want to exit?").setNegativeButton(android.R.string.no, null).setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                public void onClick(DialogInterface arg0, int arg1) {
                    MainActivity.super.onBackPressed();
                }
            }).create().show();
        }
        else if (count > 1) {
            getFragmentManager().popBackStack();
        }
    }

    @Subscribe
    public void subscribeOnCampaignDataEvent(CampaignDataEvent event) {
        try {
            JSONArray jArray = null;
            jArray = (JSONArray) event.getData().getJSONArray("items");
            Utility.getInstance(getApplicationContext()).parseCampaignData(jArray);

            FragmentManager fragmentManager = getFragmentManager();
            ContactYourLegislatorsListFragment fragment = new ContactYourLegislatorsListFragment();
            fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).addToBackStack(Utility.getInstance().CONTACT_YOUR_LEGISLATORS).commit();

        }
        catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Subscribe
    public void subscribeOnPDFDownload(PdfDataEvent event) {

        showProgressDialog("American Hospital Association","Opening Download ...");

        if (Utility.getInstance().canDisplayPdf(mContext)) {
            try {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                String externalStorage = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getPath();
                intent.setDataAndType(Uri.fromFile(new File(externalStorage + "/" + event.getDataString())), "application/pdf");
                startActivity(intent);
                progressDialog.dismiss();
                //getFragmentManager().popBackStack();
            }
            catch (ActivityNotFoundException e) {
                progressDialog.dismiss();
                new AlertDialog.Builder(this).setTitle("American Hospital Association").setMessage("PDF error.").setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        //getFragmentManager().popBackStack();
                    }
                }).show();
            }
            catch (Exception e) {
                Log.d(TAG, "debug");
                e.printStackTrace();
            }
        }
        else {
            progressDialog.dismiss();

            new AlertDialog.Builder(this).setTitle("American Hospital Association")
                    .setMessage("No PDF viewer installed.  Please download pdf viewer from Google Play Store.")
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            //getFragmentManager().popBackStack();
                        }
                    }).show();

        }
    }


    @Subscribe
    public void subcribeVoterVoiceEvents(VoterVoiceDataEvent event) {

        closeProgressDialog();

        if (event.getTagName().equals(VoterVoiceDataEvent.VOTER_VOICE_CREATE_DATA)) {

            OAMItem oamItem = Utility.getInstance(mContext).getLoginData();

            Log.d(TAG, "debug");
            URL url = null;

            try {

                String urlString = getResources().getString(R.string.vv_matches_campaign_url);
                urlString = urlString.replace("mId", "30763");
                urlString = urlString.replace("mToken", URLEncoder.encode((oamItem.token != null ? oamItem.token : ""), "UTF-8"));

                Log.d(TAG, urlString);

                url = new URL(urlString);

            }
            catch (MalformedURLException e) {
                e.printStackTrace();
            }
            catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }

            if (Utility.getInstance().isNetworkAvailable(this)) {
                new VoterVoiceMatchesCampaignAsyncTask(url, getApplicationContext(), this).execute();
            }

        }
        else if (event.getTagName().equals(VoterVoiceDataEvent.VOTER_VOICE_GET_CAMPAIGN_LIST_DATA)) {

            Log.d(TAG, "debug");
            Fragment fragment = new CampaignSummaryListFragment();
            addToAppBackStack(fragment, "campaign-summary", "Campaigns");


        }
        else if (event.getTagName().equals(VoterVoiceDataEvent.VOTER_VOICE_GET_CAMPAIGN_DATA)) {

            Log.d(TAG, "debug");

            Fragment fragment = new DirectoryListFragment();
            addToFragmentBackStack(fragment, Utility.getInstance().DIRECTORY, "Directory");


        }
        else if (event.getTagName().equals(VoterVoiceDataEvent.VOTER_VOICE_POST_DATA)) {

        }
        else if (event.getTagName().equals(VoterVoiceDataEvent.VOTER_VOICE_GET_TARGETED_MESSAGE_DATA)) {

        }
        else if (event.getTagName().equals(VoterVoiceDataEvent.VOTER_VOICE_GET_MATCHES_FOR_CAMPAIGN_DATA)) {

        }
    }

}