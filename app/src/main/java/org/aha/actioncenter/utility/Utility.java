package org.aha.actioncenter.utility;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

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

    private static final Utility INSTANCE  = new Utility();
    private static final String TAG = "Utility";
    private static Context mContext;
    private static boolean mDataLoaded = false;

    public static String ACTION_CENTER = "action-center";
    public static String ADDITIONAL_INFO = "additional-info";
    public static String LETTER = "letter";
    public static String PRESS_RELEASE = "press-release";
    public static String TESTIMONY = "testimony";
    public static String ADVISORY = "advisory";
    public static String BULLETIN = "bulletin";
    public static String ISSUE_PAPERS = "issue-papers";
    public static String ACTION_ALERT = "action-alert";
    public static String FACT_SHEET = "fact-sheet";

    private Utility(){}

    public static Utility getInstance(){
        return INSTANCE;
    }

    public static Utility getInstance(Context context){
        mContext = context;
        return INSTANCE;
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

    public void parseFeedData(JSONArray jArray){

        Gson gson = null;
        int count = jArray.length();

        mDataLoaded = false;

        ArrayList<FeedItem> additionalInfo = new ArrayList<FeedItem>();
        ArrayList<FeedItem> letter = new ArrayList<FeedItem>();
        ArrayList<FeedItem> pressRelease = new ArrayList<FeedItem>();
        ArrayList<FeedItem> testimony = new ArrayList<FeedItem>();
        ArrayList<FeedItem> advisory = new ArrayList<FeedItem>();
        ArrayList<FeedItem> bulletin = new ArrayList<FeedItem>();
        ArrayList<FeedItem> issuePapers = new ArrayList<FeedItem>();
        ArrayList<FeedItem> actionAlert = new ArrayList<FeedItem>();
        ArrayList<FeedItem> factSheet = new ArrayList<FeedItem>();

        for(int i=0; i < count; i++){

            gson = new Gson();
            FeedItem item = null;
            try {
                item = gson.fromJson(jArray.get(i).toString(), FeedItem.class);
            }
            catch (JSONException e) {
                e.printStackTrace();
            }

            if(item.ContentType.equals(ADDITIONAL_INFO)){
                additionalInfo.add(item);
            }
            else if(item.ContentType.equals(LETTER)){
                letter.add(item);
            }
            else if(item.ContentType.equals(PRESS_RELEASE)){
                pressRelease.add(item);
            }
            else if(item.ContentType.equals(TESTIMONY)){
               testimony.add(item);
            }
            else if(item.ContentType.equals(ADVISORY)){
                advisory.add(item);
            }
            else if(item.ContentType.equals(BULLETIN)){
                bulletin.add(item);
            }
            else if(item.ContentType.equals(ISSUE_PAPERS)){
                issuePapers.add(item);
            }
            else if(item.ContentType.equals(ACTION_ALERT)){
                actionAlert.add(item);
            }
            else if(item.ContentType.equals(FACT_SHEET)){
                factSheet.add(item);
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
        saveFeedData(ISSUE_PAPERS, issuePapers);
        saveFeedData(ACTION_ALERT, actionAlert);
        saveFeedData(FACT_SHEET, factSheet);

        mDataLoaded = true;
    }

    private void saveFeedData(String dataName, List<FeedItem> list){

        SharedPreferences prefs = mContext.getSharedPreferences(dataName, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();

        Gson gson = new Gson();

        if(list.size() > 0) {
            editor.putString(dataName, gson.toJson(list));
        }
        else {
            editor.putString(dataName, "");
        }

        editor.apply();

        Log.d(TAG, "Utility->saveFeedData");

    }

    public List<FeedItem> getFeedData(String dataName){

        SharedPreferences prefs = mContext.getSharedPreferences(dataName, Context.MODE_PRIVATE);
        String dataString = prefs.getString(dataName, "");

        Gson gson = new Gson();

        Type feedItemArrayListType = new TypeToken<ArrayList<FeedItem>>(){}.getType();

        ArrayList<FeedItem> list = gson.fromJson(dataString, feedItemArrayListType);

        Log.d(TAG, "Utility->getFeedData");

        return list;
    }

    public boolean isDataLoaded(){
        return mDataLoaded;
    }


    public boolean hasData(String dataName) {
        SharedPreferences prefs = mContext.getSharedPreferences(dataName, Context.MODE_PRIVATE);
        String dataString = prefs.getString(dataName, "");
        return dataString.length()>0;
    }
}
