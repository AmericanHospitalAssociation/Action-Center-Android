package org.aha.actioncenter;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBarDrawerToggle;
import android.util.Log;
import android.util.SparseArray;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ExpandableListView;
import android.widget.ListView;

import com.crashlytics.android.Crashlytics;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import com.squareup.otto.Subscribe;
import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.TwitterAuthConfig;

import org.aha.actioncenter.data.AHAExpandableListAdapter;
import org.aha.actioncenter.events.FeedDataEvent;
import org.aha.actioncenter.models.NavigationItem;
import org.aha.actioncenter.service.FeedAsyncTask;
import org.aha.actioncenter.utility.AHABusProvider;
import org.aha.actioncenter.utility.Utility;
import org.aha.actioncenter.views.ActionAlertListFragment;
import org.aha.actioncenter.views.AdditionalInfoListFragment;
import org.aha.actioncenter.views.AdvisoryListFragment;
import org.aha.actioncenter.views.FactSheetListFragment;
import org.aha.actioncenter.views.LetterListFragment;
import org.aha.actioncenter.views.SpecialBulletins;
import org.aha.actioncenter.views.TestimonyListFragment;
import org.aha.actioncenter.models.NavigationGroup;
import org.json.JSONArray;
import org.json.JSONException;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.lang.reflect.Type;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import io.fabric.sdk.android.Fabric;


public class MainActivity extends ActionBarActivity {

    private static final String TAG = "MainActivity";
    private String[] mActionCenter;
    private String[] mActionCenterIds;
    private String[] mMainNavigation;
    private String[] mMainNavigationIds;
    private DrawerLayout mDrawerLayout;
    //private ListView mDrawerList;
    private ExpandableListView mDrawerList;
    private ActionBarDrawerToggle mDrawerToggle;
    private CharSequence mDrawerTitle;
    private CharSequence mTitle;

    private Context mContext = null;

    // Note: Your consumer key and secret should be obfuscated in your source code before shipping.

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mContext = getApplicationContext();

        //Register bus provider to listen to events.
        AHABusProvider.getInstance().register(this);
        //TwitterAuthConfig authConfig = new TwitterAuthConfig(getResources().getString(R.string.twitter_key), getResources().getString(R.string.twitter_secret));
        //Fabric.with(this, new Crashlytics(), new Twitter(authConfig));

        setContentView(R.layout.activity_main);

        URL url = null;
        try {
            url = new URL(getResources().getString(R.string.feed_url));
        }
        catch (MalformedURLException e) {
            e.printStackTrace();
        }


        FeedAsyncTask asyncTask = new FeedAsyncTask(url, mContext);
        asyncTask.execute();


        mTitle = mDrawerTitle = getTitle();

        NavigationGroup group = null;

        Reader reader = null;
        InputStream stream = getResources().openRawResource(R.raw.navigation);
        reader = new BufferedReader(new InputStreamReader(stream), 8092);

        // parse json
        JsonParser parser = new JsonParser();
        JsonObject jNavigation = (JsonObject)parser.parse(reader);

        Type listType = new TypeToken<ArrayList<NavigationItem>>() {}.getType();
        ArrayList<NavigationItem> navigationItemArrayList = new Gson().fromJson(jNavigation.getAsJsonArray("topnav"), listType);

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        //mDrawerList = (ListView) findViewById(R.id.left_drawer);
        mDrawerList = (ExpandableListView) findViewById(R.id.left_drawer);

        // Set the adapter for the list view
        //mDrawerList.setAdapter(new ArrayAdapter<String>(this, R.layout.drawer_list_item, mActionCenter));
        AHAExpandableListAdapter adapter = new AHAExpandableListAdapter(this, navigationItemArrayList);
        mDrawerList.setAdapter(adapter);
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
    }

    /* Called whenever we call invalidateOptionsMenu() */
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        // If the nav drawer is open, hide action items related to the content view
        boolean drawerOpen = mDrawerLayout.isDrawerOpen(mDrawerList);
        //menu.findItem(R.id.action_websearch).setVisible(!drawerOpen);
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
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //AHABusProvider.getInstance().unregister(this);
    }


    /**
     * Swaps fragments in the main content view
     */
    public void selectItem(String navigationName) {
        Log.d(TAG, "Navigation item selected.");

        Fragment fragment = null;
        Bundle args = new Bundle();
        //args.putInt(PlanetFragment.ARG_PLANET_NUMBER, position);
        //fragment.setArguments(args);

        // Insert the fragment by replacing any existing fragment
        FragmentManager fragmentManager = getFragmentManager();

        if (navigationName.equals(Utility.getInstance().ACTION_ALERT))
            fragment = new ActionAlertListFragment();
        if (navigationName.equals(Utility.getInstance().FACT_SHEET))
            fragment = new FactSheetListFragment();
        if (navigationName.equals(Utility.getInstance().BULLETIN))
            fragment = new SpecialBulletins();
        if (navigationName.equals(Utility.getInstance().ADVISORY))
            fragment = new AdvisoryListFragment();
        if (navigationName.equals(Utility.getInstance().LETTER))
            fragment = new LetterListFragment();
        if (navigationName.equals(Utility.getInstance().TESTIMONY))
            fragment = new TestimonyListFragment();
        if (navigationName.equals(Utility.getInstance().ADDITIONAL_INFO))
            fragment = new AdditionalInfoListFragment();

        if(fragment != null) {
            fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).commit();

            // Highlight the selected item, update the title, and close the drawer
            //mDrawerList.setItemChecked(position, true);
            setTitle(navigationName);
            //mDrawerLayout.closeDrawer(mDrawerList);
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
}
