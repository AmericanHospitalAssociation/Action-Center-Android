package org.aha.actioncenter;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
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

    protected Button login_btn = null;
    protected Button create_account_btn = null;
    protected Button forgot_password_btn = null;
    protected Button need_help_btn = null;

    protected TextView username_txt;
    protected TextView password_txt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.login_fragment_view);

        ImageView imageView = (ImageView) findViewById(android.R.id.home);
        imageView.setPadding(10, 0, 0, 0);

        AHABusProvider.getInstance().register(this);

        Button login_btn = (Button) findViewById(R.id.login_btn);
        login_btn.setOnClickListener(this);

        Button create_account_btn = (Button) findViewById(R.id.create_account_btn);
        create_account_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Uri uriUrl = Uri.parse(getString(R.string.create_account_url));
                Intent launchBrowser = new Intent(Intent.ACTION_VIEW, uriUrl);
                startActivity(launchBrowser);
            }
        });

        Button forgot_password_btn = (Button) findViewById(R.id.forgot_password_btn);
        forgot_password_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Uri uriUrl = Uri.parse(getString(R.string.forgot_password_url));
                Intent launchBrowser = new Intent(Intent.ACTION_VIEW, uriUrl);
                startActivity(launchBrowser);
            }
        });

        Button need_help_btn = (Button) findViewById(R.id.need_help_btn);
        need_help_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Uri uriUrl = Uri.parse(getString(R.string.need_help_url));
                Intent launchBrowser = new Intent(Intent.ACTION_VIEW, uriUrl);
                startActivity(launchBrowser);
            }
        });

        username_txt = (TextView) findViewById(R.id.username_txt);
        password_txt = (TextView) findViewById(R.id.password_txt);


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
            showErrorDialog();
        }

        Log.d(TAG, "debug");

    }

    @Subscribe
    public void subscribeLoginDataEvent(LoginEvent event) {

        SharedPreferences prefs = getSharedPreferences("login", Context.MODE_PRIVATE);
        String dataString = prefs.getString("login", "");
        Gson gson = new Gson();
        OAMItem omaItem = gson.fromJson(dataString, OAMItem.class);

        if (!omaItem.ahaid.isEmpty()) {
            ((AHAActionCenterApplication) getApplicationContext()).pullAdditionalData(this);
        }
        else {
            Log.d(TAG, "Login Unsuccessful");
            showErrorDialog();
        }

    }

    private void showErrorDialog() {
        new AlertDialog.Builder(this).setTitle("Invalid Login Credentials").setMessage("The Email Address or Password you entered is incorrect. Please verify your credentials and try again.").setIcon(android.R.drawable.ic_dialog_alert).setCancelable(false).setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                // continue with delete
                dialog.dismiss();
            }
        }).show();
    }

}