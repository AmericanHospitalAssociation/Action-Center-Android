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

import org.aha.actioncenter.models.CampaignSummaryItem;
import org.aha.actioncenter.models.CampaignItem;
import org.aha.actioncenter.models.CampaignUserItem;
import org.aha.actioncenter.models.EventItem;
import org.aha.actioncenter.models.FeedItem;
import org.aha.actioncenter.models.NewsItem;
import org.aha.actioncenter.models.OAMItem;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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
    private static boolean mNewsDataLoaded = false;
    private static boolean mCampaignDataLoaded = false;
    private static boolean mDirectoryDataLoaded = false;
    private static boolean mCampaignSummaryDataLoaded = false;

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
    public static String NEWS = "aha-news";
    public static String CONTACT_YOUR_LEGISLATORS = "contact-your-legislators";
    public static String TWITTER_FEEDS = "twitter-feeds";
    public static String DIRECTORY = "directory";
    public static String CAMPAIGN_SUMMARY_LIST = "campaign-summary-list";

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


            if (item.isHidden == 0) {
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
            }
        }

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

        }

        saveEventData(EVENTS, events);

        mEventDataLoaded = true;
    }


    public void parseNewsData(JSONArray jArray) {

        Gson gson = null;
        int count = jArray.length();

        mNewsDataLoaded = false;

        ArrayList<NewsItem> newsList = new ArrayList<NewsItem>();

        for (int i = 0; i < count; i++) {

            gson = new Gson();
            NewsItem item = null;
            try {
                item = gson.fromJson(jArray.get(i).toString(), NewsItem.class);
            }
            catch (JSONException e) {
                e.printStackTrace();
            }

            newsList.add(item);

        }

        saveNewsData(NEWS, newsList);

        mNewsDataLoaded = true;
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

    public void saveEventData(String dataName, List<EventItem> list) {

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
    }

    public void saveLoginData(String dataName, JSONObject item) {
        SharedPreferences prefs = mContext.getSharedPreferences(dataName, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();

        Gson gson = new Gson();
        editor.putString(dataName, item.toString());

        editor.apply();
    }

    public void saveLoginData(String dataName, OAMItem item) {
        SharedPreferences prefs = mContext.getSharedPreferences(dataName, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();

        Gson gson = new Gson();
        editor.putString(dataName, gson.toJson(item));
        editor.apply();
    }

    public OAMItem getLoginData(String dataName) {

        SharedPreferences prefs = mContext.getSharedPreferences(dataName, Context.MODE_PRIVATE);
        String dataString = prefs.getString(dataName, "");

        Gson gson = new Gson();

        Type oamItemArrayListType = new TypeToken<OAMItem>() {
        }.getType();

        OAMItem item = gson.fromJson(dataString, oamItemArrayListType);

        return item;
    }


    public List<FeedItem> getFeedData(String dataName) {

        SharedPreferences prefs = mContext.getSharedPreferences(dataName, Context.MODE_PRIVATE);
        String dataString = prefs.getString(dataName, "");

        Gson gson = new Gson();

        Type feedItemArrayListType = new TypeToken<ArrayList<FeedItem>>() {
        }.getType();

        ArrayList<FeedItem> list = gson.fromJson(dataString, feedItemArrayListType);

        return list;
    }

    public List<NewsItem> getNewsData(String dataName) {
        SharedPreferences prefs = mContext.getSharedPreferences(dataName, Context.MODE_PRIVATE);
        String dataString = prefs.getString(dataName, "");

        Gson gson = new Gson();

        Type newsItemArrayListType = new TypeToken<ArrayList<NewsItem>>() {
        }.getType();

        ArrayList<NewsItem> list = gson.fromJson(dataString, newsItemArrayListType);

        return list;
    }

    private void saveNewsData(String dataName, List<NewsItem> list) {

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
    }

    public List<EventItem> getEventData(String dataName) {

        SharedPreferences prefs = mContext.getSharedPreferences(dataName, Context.MODE_PRIVATE);
        String dataString = prefs.getString(dataName, "");

        Gson gson = new Gson();

        Type eventItemArrayListType = new TypeToken<ArrayList<EventItem>>() {
        }.getType();

        ArrayList<EventItem> list = gson.fromJson(dataString, eventItemArrayListType);


        return list;
    }

    public List<CampaignUserItem> getDirectoryData() {

        SharedPreferences prefs = mContext.getSharedPreferences("directory", Context.MODE_PRIVATE);
        String dataString = prefs.getString("directory", "");

        Type campaignItemArrayListType = new TypeToken<ArrayList<CampaignUserItem>>(){}.getType();

        ArrayList<CampaignUserItem> list = new Gson().fromJson(dataString, campaignItemArrayListType);

        return list;

    }



    public void parseCampaignData(JSONArray jArray) {

        Gson gson = null;
        int count = jArray.length();

        mCampaignDataLoaded = false;

        ArrayList<CampaignItem> campaignList = new ArrayList<CampaignItem>();

        for (int i = 0; i < count; i++) {

            gson = new Gson();
            CampaignItem item = null;
            try {
                item = gson.fromJson(jArray.get(i).toString(), CampaignItem.class);
            }
            catch (JSONException e) {
                e.printStackTrace();
            }

            campaignList.add(item);

        }

        saveCampaignData(CONTACT_YOUR_LEGISLATORS, campaignList);

        mCampaignDataLoaded = true;
    }


    private void saveCampaignData(String dataName, List<CampaignItem> list) {

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
    }


    public List<CampaignItem> getCampaignData(String dataName) {
        SharedPreferences prefs = mContext.getSharedPreferences(dataName, Context.MODE_PRIVATE);
        String dataString = prefs.getString(dataName, "");

        Gson gson = new Gson();
        Type campaignItemArrayListType = new TypeToken<ArrayList<CampaignItem>>(){}.getType();
        ArrayList<CampaignItem> list = gson.fromJson(dataString, campaignItemArrayListType);

        return list;
    }

    public List<CampaignSummaryItem> getCampaignSummaryData() {

        SharedPreferences prefs = mContext.getSharedPreferences(CAMPAIGN_SUMMARY_LIST, Context.MODE_PRIVATE);
        String dataString = prefs.getString(CAMPAIGN_SUMMARY_LIST, "");

        JSONObject jsonobj = null;
        ArrayList<CampaignSummaryItem> list = new ArrayList<CampaignSummaryItem>();

        try {
            jsonobj = new JSONObject(dataString);
            JSONArray jsonArray = jsonobj.getJSONArray("values");
            for(int i = 0; i < jsonArray.length(); i ++){
                CampaignSummaryItem item = null;
                item = new CampaignSummaryItem();
                JSONObject jObj = jsonArray.getJSONObject(i).getJSONObject("nameValuePairs");
                item = new Gson().fromJson(jObj.toString(), CampaignSummaryItem.class);
                list.add(item);
            }

            Log.d(TAG, "debug");
        }
        catch (JSONException e) {
            e.printStackTrace();
        }

        return list;
    }

    public void saveCampaignSummaryData(JSONObject json) {
        Log.d(TAG, "debug");

        mCampaignSummaryDataLoaded = false;

        SharedPreferences prefs = mContext.getSharedPreferences(CAMPAIGN_SUMMARY_LIST, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();

        try {
            editor.putString(CAMPAIGN_SUMMARY_LIST, new Gson().toJson(json.getJSONObject("response").getJSONArray("body")));
        }
        catch (JSONException e) {
            e.printStackTrace();
        }

        mCampaignSummaryDataLoaded = true;

        editor.apply();
    }


    public boolean isFeedDataLoaded() {
        return mFeedDataLoaded;
    }

    public boolean isEventDataLoaded() {
        return mEventDataLoaded;
    }

    public boolean isNewsDataLoaded() {
        return mNewsDataLoaded;
    }

    public boolean isCampaignDataLoaded() {
        return mCampaignDataLoaded;
    }

    public boolean isCampaignSummaryDataLoaded() {
        return mCampaignSummaryDataLoaded;
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
        }
        else {
            return false;
        }
    }

    public void logoutCurrentUser() {
        SharedPreferences prefs = mContext.getSharedPreferences("login", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();

        editor.remove("login");
        editor.commit();
    }

    public boolean contentLoaded() {
        return mFeedDataLoaded && mNewsDataLoaded && mEventDataLoaded;
    }

    public void saveCampaignMatches(JSONObject json) {

        ArrayList<CampaignUserItem> directoryItemsArray = new ArrayList<CampaignUserItem>();
        ArrayList<CampaignUserItem> usSenatorsItemArray = new ArrayList<CampaignUserItem>();
        ArrayList<CampaignUserItem> usRepresentativeItemArray = new ArrayList<CampaignUserItem>();

        Type campaignItemArrayListType = new TypeToken<ArrayList<CampaignUserItem>>(){}.getType();

        SharedPreferences prefs;
        SharedPreferences.Editor editor;

        try {
            JSONArray jArray = json.getJSONObject("response").getJSONArray("body");

            int icount = 0;
            icount = jArray.length();

            for(int i = 0; i < icount ; i++){
                JSONObject jObj = null;
                jObj = jArray.getJSONObject(i);

                if(jObj.get("groupId").equals("US Senators")){
                    usSenatorsItemArray = new Gson().fromJson(jObj.getJSONArray("matches").toString(), campaignItemArrayListType);
                    int jcount = usSenatorsItemArray.size();
                    for(int j = 0; j < jcount; j++){
                        CampaignUserItem item = usSenatorsItemArray.get(j);
                        item.groupId = jObj.getString("groupId");
                        usSenatorsItemArray.set(j, item);
                    }
                }
                if(jObj.get("groupId").equals("US Representative")){
                    usRepresentativeItemArray = new Gson().fromJson(jObj.getJSONArray("matches").toString(), campaignItemArrayListType);
                    int jcount = usRepresentativeItemArray.size();
                    for(int j = 0; j < jcount; j++){
                        CampaignUserItem item = usRepresentativeItemArray.get(j);
                        item.groupId = jObj.getString("groupId");
                        usRepresentativeItemArray.set(j, item);
                    }
                }
            }

            directoryItemsArray.addAll(usRepresentativeItemArray);
            directoryItemsArray.addAll(usSenatorsItemArray);

            prefs = mContext.getSharedPreferences("directory", Context.MODE_PRIVATE);
            editor = prefs.edit();
            if (usSenatorsItemArray.size() > 0) {
                editor.putString("directory", new Gson().toJson(directoryItemsArray));
            }
            else {
                editor.putString("directory", "");
            }
            editor.apply();

            Log.d(TAG, "debug");
            mDirectoryDataLoaded = true;

        }
        catch (JSONException e) {
            e.printStackTrace();
            mDirectoryDataLoaded = false;
        }
    }

    public boolean isDirectoryDataLoaded() {
        return mDirectoryDataLoaded;
    }


}
