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

    private Utility(){}

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

        ArrayList<FeedItem> additionalInfo = new ArrayList<FeedItem>();
        ArrayList<FeedItem> letter = new ArrayList<FeedItem>();
        ArrayList<FeedItem> pressRelease = new ArrayList<FeedItem>();
        ArrayList<FeedItem> testimony = new ArrayList<FeedItem>();
        ArrayList<FeedItem> advisory = new ArrayList<FeedItem>();
        ArrayList<FeedItem> bulletin = new ArrayList<FeedItem>();
        ArrayList<FeedItem> issuePapers = new ArrayList<FeedItem>();
        ArrayList<FeedItem> actionAlert = new ArrayList<FeedItem>();

        for(int i=0; i < count; i++){

            gson = new Gson();
            FeedItem item = null;
            try {
                item = gson.fromJson(jArray.get(i).toString(), FeedItem.class);
            }
            catch (JSONException e) {
                e.printStackTrace();
            }

            if(item.ContentType.equals("additional-info")){
                additionalInfo.add(item);
            }
            else if(item.ContentType.equals("letter")){
                letter.add(item);
            }
            else if(item.ContentType.equals("press-release")){
                pressRelease.add(item);
            }
            else if(item.ContentType.equals("testimony")){
               testimony.add(item);
            }
            else if(item.ContentType.equals("advisory")){
                advisory.add(item);
            }
            else if(item.ContentType.equals("bulletin")){
                bulletin.add(item);
            }
            else if(item.ContentType.equals("issue-papers")){
                issuePapers.add(item);
            }
            else if(item.ContentType.equals("action-alert")){
                actionAlert.add(item);
            }
            Log.d(TAG, "Utility->parseFeedData");
        }

        Log.d(TAG, "Utility");

        saveFeedData("additional-info", additionalInfo);
        saveFeedData("letter", letter);
        saveFeedData("press-release", pressRelease);
        saveFeedData("testimony", testimony);
        saveFeedData("advisory", advisory);
        saveFeedData("bulletin", bulletin);
        saveFeedData("issue-papers", issuePapers);
        saveFeedData("action-alert", actionAlert);

    }

    private void saveFeedData(String dataName, List<FeedItem> list){

        SharedPreferences prefs = mContext.getSharedPreferences(dataName, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();

        Gson gson = new Gson();
        editor.putString(dataName, gson.toJson(list));
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

}
