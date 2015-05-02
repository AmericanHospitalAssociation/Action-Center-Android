package org.aha.actioncenter;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.squareup.otto.Subscribe;

import org.aha.actioncenter.events.LoginEvent;
import org.aha.actioncenter.models.OAMItem;
import org.aha.actioncenter.service.LoginAsyncTask;
import org.aha.actioncenter.utility.AHABusProvider;

import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by markusmcgee on 4/23/15.
 */
public class LoginActivity extends Activity implements View.OnClickListener {

    private static final String TAG = "LoginActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.login_fragment_view);

        ImageView imageView = (ImageView)findViewById(android.R.id.home);
        imageView.setPadding(10, 0, 0, 0);

        AHABusProvider.getInstance().register(this);

        Button button = (Button) findViewById(R.id.login_btn);
        button.setOnClickListener(this);

    }

    @Override
    protected void onPause() {
        super.onPause();
        AHABusProvider.getInstance().unregister(this);

    }

    @Override
    protected void onResume() {
        super.onResume();
        AHABusProvider.getInstance().register(this);

    }

    @Override
    public void onClick(View view) {
        TextView username_txt;
        TextView password_txt;

        username_txt = (TextView) findViewById(R.id.username_txt);
        password_txt = (TextView) findViewById(R.id.password_txt);

        String loginUrl = getResources().getString(R.string.login_url);
        loginUrl = loginUrl.replace("mEmail", username_txt.getText().toString());
        loginUrl = loginUrl.replace("mPassword", password_txt.getText().toString());

        Log.d(TAG, loginUrl);

        try {
            URL url = new URL(loginUrl);
            new LoginAsyncTask(url, getApplicationContext(), this).execute();
        }
        catch (MalformedURLException e) {
            e.printStackTrace();
        }

        Log.d(TAG, "debug");

    }

    @Subscribe
    public void subscribeLoginDataEvent(LoginEvent event){

        SharedPreferences prefs = getSharedPreferences("login", Context.MODE_PRIVATE);
        String dataString = prefs.getString("login", "");
        Gson gson = new Gson();
        OAMItem omaItem = gson.fromJson(dataString, OAMItem.class);

        if(!omaItem.ahaid.isEmpty()){
            ((AHAActionCenterApplication)getApplicationContext()).pullAdditionalData(this);
        }
        else{
            Log.d(TAG, "Login Unsuccessful");
        }

    }

}