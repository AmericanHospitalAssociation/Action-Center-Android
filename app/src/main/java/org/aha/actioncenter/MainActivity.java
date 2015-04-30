package org.aha.actioncenter;

import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBarDrawerToggle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ExpandableListView;
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
import org.aha.actioncenter.models.NavigationItem;
import org.aha.actioncenter.models.OAMItem;
import org.aha.actioncenter.service.CampaignAsyncTask;
import org.aha.actioncenter.utility.AHABusProvider;
import org.aha.actioncenter.utility.Utility;
import org.aha.actioncenter.views.ActionAlertListFragment;
import org.aha.actioncenter.views.AdditionalInfoListFragment;
import org.aha.actioncenter.views.AdvisoryListFragment;
import org.aha.actioncenter.views.CongressionalCalendarFragment;
import org.aha.actioncenter.views.ContactYourLegislatorsListFragment;
import org.aha.actioncenter.views.EventsListFragment;
import org.aha.actioncenter.views.FactSheetListFragment;
import org.aha.actioncenter.views.HomeFragment;
import org.aha.actioncenter.views.LetterListFragment;
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
import java.lang.reflect.Type;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class MainActivity extends ActionBarActivity implements ExpandableListView.OnGroupClickListener, ExpandableListView.OnChildClickListener, View.OnClickListener, FragmentManager.OnBackStackChangedListener {

    private static final String TAG = "MainActivity";
    private DrawerLayout mDrawerLayout;
    private ExpandableListView mDrawerList;
    private ActionBarDrawerToggle mDrawerToggle;
    private CharSequence mDrawerTitle;
    private CharSequence mTitle;

    private Context mContext = null;
    private ArrayList<NavigationItem> navigationItemArrayList = null;
    private AHAExpandableListAdapter navigationAdapter = null;

    private ProgressDialog progressDialog;

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
                getSupportActionBar().setTitle(mTitle);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }

            /** Called when a drawer has settled in a completely open state. */
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                getSupportActionBar().setTitle(mDrawerTitle);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }
        };

        // Set the drawer toggle as the DrawerListener
        mDrawerLayout.setDrawerListener(mDrawerToggle);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

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
        super.onResume();
        AHABusProvider.getInstance().register(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        AHABusProvider.getInstance().unregister(this);
        if (progressDialog != null && progressDialog.isShowing())
            progressDialog.dismiss();
    }

    /**
     * Swaps fragments in the main content view
     */
    public void selectItem(NavigationItem item) {
        Log.d(TAG, "Navigation item selected.");

        Fragment fragment = null;
        Bundle args = new Bundle();
        //fragment.setArguments(args);

        // Insert the fragment by replacing any existing fragment
        FragmentManager fragmentManager = getFragmentManager();

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
        if (item.id.equals(Utility.getInstance().CONGRESSIONAL_CALENDAR))
            fragment = new CongressionalCalendarFragment();
        if (item.id.equals(Utility.getInstance().WORKING_WITH_CONGRESS))
            fragment = new WorkingWithCongressFragment();
        if (item.id.equals(Utility.getInstance().EVENTS))
            fragment = new EventsListFragment();
        if (item.id.equals(Utility.getInstance().NEWS))
            fragment = new NewsListFragment();
        if (item.id.equals(Utility.getInstance().DIRECTORY)) {

            SharedPreferences prefs = getSharedPreferences("login", Context.MODE_PRIVATE);
            String dataString = prefs.getString("login", "");
            Gson gson = new Gson();
            Type oamType = new TypeToken<OAMItem>() {
            }.getType();
            OAMItem omaItem = gson.fromJson(dataString, oamType);


        }
        if (item.id.equals(Utility.getInstance().CONTACT_YOUR_LEGISLATORS)) {
            try {
                URL url = new URL(getResources().getString(R.string.campaign_url));
                CampaignAsyncTask asyncTask = new CampaignAsyncTask(url, mContext, this);
            }
            catch (MalformedURLException e) {
                e.printStackTrace();
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (item.id.equals(Utility.getInstance().TWITTER_FEEDS)) {
            args.putString(item.id, item.user);
            fragment = new TwitterFeedListFragment();
            fragment.setArguments(args);
        }
        if (fragment != null) {

            fragmentManager.addOnBackStackChangedListener(this);
            //fragmentManager.beginTransaction().add(R.id.content_frame, fragment).addToBackStack(item.id).commit();
            fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).addToBackStack(item.id).commit();

            // Highlight the selected item, update the title, and close the drawer
            setTitle(item.name);
            mDrawerLayout.closeDrawer(mDrawerList);
        }
    }

    @Override
    public void setTitle(CharSequence title) {
        mTitle = title;
        getSupportActionBar().setTitle(mTitle);
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
        if (navigationItem.id.equals(Utility.getInstance().HOME) ||
                navigationItem.id.equals(Utility.getInstance().EVENTS) ||
                navigationItem.id.equals(Utility.getInstance().NEWS)) {
            selectItem(navigationItem);
        }
        return false;
    }

    @Override
    public boolean onChildClick(ExpandableListView expandableListView, View view, int groupPosition, int childPosition, long id) {
        Log.d(TAG, "onChildClick");
        NavigationItem navigationItem = (NavigationItem) navigationAdapter.getChild(groupPosition, childPosition);
        selectItem(navigationItem);
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
        if (fragment instanceof CongressionalCalendarFragment)
            setTitle("Congressional Calendar");
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
        if (count == 0) {
            super.onBackPressed();
        }
        else {
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
            fragmentManager.beginTransaction().add(R.id.content_frame, fragment).addToBackStack(Utility.getInstance().CONTACT_YOUR_LEGISLATORS).commit();

        }
        catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Subscribe
    public void subscribeOnPDFDownload(PdfDataEvent event) {

        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("American Hospital Association");
        progressDialog.setMessage("Opening Download ...");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();

        if (Utility.getInstance().canDisplayPdf(mContext)) {
            try {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                String externalStorage = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getPath();
                intent.setDataAndType(Uri.fromFile(new File(externalStorage + "/" + event.getDataString())), "application/pdf");
                startActivity(intent);
                progressDialog.dismiss();
                getFragmentManager().popBackStack();
            }
            catch (ActivityNotFoundException e) {
                progressDialog.dismiss();
                new AlertDialog.Builder(this).setTitle("American Hospital Association").setMessage("PDF error.").setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        progressDialog.dismiss();
                        getFragmentManager().popBackStack();
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

            new AlertDialog.Builder(this).setTitle("American Hospital Association").setMessage("No PDF viewer installed.  Please download pdf viewer from Google Play Store.").setPositiveButton("OK", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    progressDialog.dismiss();
                    getFragmentManager().popBackStack();
                }
            }).show();

        }
    }


    @Subscribe
    public void subcribeVoterVoiceEvents(VoterVoiceDataEvent event) {
        if (event.getTagName().equals(VoterVoiceDataEvent.VOTER_VOICE_CREATE_DATA)) {

        }
        else if (event.getTagName().equals(VoterVoiceDataEvent.VOTER_VOICE_GET_CAMPAIGN_DATA)) {

        }
        else if (event.getTagName().equals(VoterVoiceDataEvent.VOTER_VOICE_POST_DATA)) {

        }
        else if (event.getTagName().equals(VoterVoiceDataEvent.VOTER_VOICE_GET_TARGETED_MESSAGE_DATA)) {

        }
        else if (event.getTagName().equals(VoterVoiceDataEvent.VOTER_VOICE_GET_MATCHES_FOR_CAMPAIGN_DATA)) {

        }
    }


}