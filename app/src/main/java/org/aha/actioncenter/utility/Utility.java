package org.aha.actioncenter.utility;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.aha.actioncenter.models.EventItem;
import org.aha.actioncenter.models.FeedItem;
import org.json.JSONArray;
import org.json.JSONException;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by markusmcgee on 4/16/15.
 */
public class Utility {

    private static final Utility INSTANCE = new Utility();
    private static final String TAG = "Utility";
    private static Context mContext;
    private static boolean mFeedDataLoaded = false;
    private static boolean mEventDataLoaded = false;

    public static String HOME = "home";
    public static String ACTION_CENTER = "action-center";
    public static String ADDITIONAL_INFO = "additional-info";
    public static String LETTER = "letter";
    public static String PRESS_RELEASE = "press-release";
    public static String TESTIMONY = "testimony";
    public static String ADVISORY = "advisory";
    public static String BULLETIN = "bulletin";
    public static String ACTION_ALERT = "action-alert";
    public static String FACT_SHEET = "issue-papers";
    public static String TWITTER_FEEDS = "twitter-feeds";

    public static String EVENTS = "events";

    public static String WORKING_WITH_CONGRESS = "working-with-congress";
    public static String CONGRESSIONAL_CALENDAR = "congressional-calendar";

    public static final String MIME_TYPE_PDF = "application/pdf";

    private Activity mActivity;

    private Utility() {
    }

    public static Utility getInstance() {
        return INSTANCE;
    }

    public static Utility getInstance(Context context) {
        mContext = context;
        return INSTANCE;
    }

    public boolean isNetworkAvailable(Activity activity) {
        mActivity = activity;
        boolean mIsNetworkAvailable = false;
        mIsNetworkAvailable = isNetworkAvailable();
        if (!mIsNetworkAvailable) {
            AlertDialog.Builder builder = new AlertDialog.Builder(mActivity);
            builder.setTitle("My Title");
            builder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    //TODO
                    dialog.dismiss();
                }
            });
            AlertDialog dialog = builder.create();
            dialog.show();

        }
        return mIsNetworkAvailable;

    }

    public boolean isNetworkAvailable() {
        ConnectivityManager cm = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();
        // if no network is available networkInfo will be null
        // otherwise check if we are connected
        if (networkInfo != null && networkInfo.isConnected()) {
            return true;
        }
        return false;
    }

    public void parseFeedData(JSONArray jArray) {

        Gson gson = null;
        int count = jArray.length();

        mFeedDataLoaded = false;

        ArrayList<FeedItem> additionalInfo = new ArrayList<FeedItem>();
        ArrayList<FeedItem> letter = new ArrayList<FeedItem>();
        ArrayList<FeedItem> pressRelease = new ArrayList<FeedItem>();
        ArrayList<FeedItem> testimony = new ArrayList<FeedItem>();
        ArrayList<FeedItem> advisory = new ArrayList<FeedItem>();
        ArrayList<FeedItem> bulletin = new ArrayList<FeedItem>();
        ArrayList<FeedItem> issuePapers = new ArrayList<FeedItem>();
        ArrayList<FeedItem> actionAlert = new ArrayList<FeedItem>();
        ArrayList<FeedItem> factSheet = new ArrayList<FeedItem>();

        ArrayList<FeedItem> workingWithCongress = new ArrayList<FeedItem>();
        ArrayList<FeedItem> congressionalCalendar = new ArrayList<FeedItem>();

        for (int i = 0; i < count; i++) {

            gson = new Gson();
            FeedItem item = null;
            try {
                item = gson.fromJson(jArray.get(i).toString(), FeedItem.class);
            }
            catch (JSONException e) {
                e.printStackTrace();
            }

            if (item.ContentType.equals(ADDITIONAL_INFO)) {
                additionalInfo.add(item);
            }
            else if (item.ContentType.equals(LETTER)) {
                letter.add(item);
            }
            else if (item.ContentType.equals(PRESS_RELEASE)) {
                pressRelease.add(item);
            }
            else if (item.ContentType.equals(TESTIMONY)) {
                testimony.add(item);
            }
            else if (item.ContentType.equals(ADVISORY)) {
                advisory.add(item);
            }
            else if (item.ContentType.equals(BULLETIN)) {
                bulletin.add(item);
            }
            else if (item.ContentType.equals(ACTION_ALERT)) {
                actionAlert.add(item);
            }
            else if (item.ContentType.equals(FACT_SHEET)) {
                factSheet.add(item);
            }
            else if (item.ContentType.equals(CONGRESSIONAL_CALENDAR)) {
                congressionalCalendar.add(item);
            }
            else if (item.ContentType.equals(WORKING_WITH_CONGRESS)) {
                workingWithCongress.add(item);
            }

            Log.d(TAG, "Utility->parseFeedData");
        }

        Log.d(TAG, "Utility");

        saveFeedData(ADDITIONAL_INFO, additionalInfo);
        saveFeedData(LETTER, letter);
        saveFeedData(PRESS_RELEASE, pressRelease);
        saveFeedData(TESTIMONY, testimony);
        saveFeedData(ADVISORY, advisory);
        saveFeedData(BULLETIN, bulletin);
        saveFeedData(ACTION_ALERT, actionAlert);
        saveFeedData(FACT_SHEET, factSheet);
        saveFeedData(WORKING_WITH_CONGRESS, workingWithCongress);
        saveFeedData(CONGRESSIONAL_CALENDAR, congressionalCalendar);

        mFeedDataLoaded = true;
    }

    public void parseEventData(JSONArray jArray) {

        Gson gson = null;
        int count = jArray.length();

        mEventDataLoaded = false;

        ArrayList<EventItem> events = new ArrayList<EventItem>();

        for (int i = 0; i < count; i++) {

            gson = new Gson();
            EventItem item = null;
            try {
                item = gson.fromJson(jArray.get(i).toString(), EventItem.class);
            }
            catch (JSONException e) {
                e.printStackTrace();
            }

                events.add(item);

            Log.d(TAG, "Utility->parseEventData");
        }

        Log.d(TAG, "Utility");

        saveEventData(EVENTS, events);

        mEventDataLoaded = true;
    }

    private void saveFeedData(String dataName, List<FeedItem> list) {

        SharedPreferences prefs = mContext.getSharedPreferences(dataName, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();

        Gson gson = new Gson();

        if (list.size() > 0) {
            editor.putString(dataName, gson.toJson(list));
        }
        else {
            editor.putString(dataName, "");
        }

        editor.apply();

        Log.d(TAG, "Utility->saveFeedData");

    }

    private void saveEventData(String dataName, List<EventItem> list) {

        SharedPreferences prefs = mContext.getSharedPreferences(dataName, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();

        Gson gson = new Gson();

        if (list.size() > 0) {
            editor.putString(dataName, gson.toJson(list));
        }
        else {
            editor.putString(dataName, "");
        }

        editor.apply();

        Log.d(TAG, "Utility->saveEventData");

    }


    public List<FeedItem> getFeedData(String dataName) {

        SharedPreferences prefs = mContext.getSharedPreferences(dataName, Context.MODE_PRIVATE);
        String dataString = prefs.getString(dataName, "");

        Gson gson = new Gson();

        Type feedItemArrayListType = new TypeToken<ArrayList<FeedItem>>() {
        }.getType();

        ArrayList<FeedItem> list = gson.fromJson(dataString, feedItemArrayListType);

        Log.d(TAG, "Utility->getFeedData");

        return list;
    }

    public List<EventItem> getEventData(String dataName) {

        SharedPreferences prefs = mContext.getSharedPreferences(dataName, Context.MODE_PRIVATE);
        String dataString = prefs.getString(dataName, "");

        Gson gson = new Gson();

        Type eventItemArrayListType = new TypeToken<ArrayList<EventItem>>() {
        }.getType();

        ArrayList<EventItem> list = gson.fromJson(dataString, eventItemArrayListType);

        Log.d(TAG, "Utility->getEventData");

        return list;
    }

    public boolean isFeedDataLoaded() {
        return mFeedDataLoaded;
    }

    public boolean isEventDataLoaded() {
        return mEventDataLoaded;
    }


    public boolean hasData(String dataName) {
        SharedPreferences prefs = mContext.getSharedPreferences(dataName, Context.MODE_PRIVATE);
        String dataString = prefs.getString(dataName, "");
        return dataString.length() > 0;
    }


    public static boolean canDisplayPdf(Context context) {
        PackageManager packageManager = context.getPackageManager();
        Intent testIntent = new Intent(Intent.ACTION_VIEW);
        testIntent.setType(MIME_TYPE_PDF);
        if (packageManager.queryIntentActivities(testIntent, PackageManager.MATCH_DEFAULT_ONLY).size() > 0) {
            return true;
        } else {
            return false;
        }
    }
}
