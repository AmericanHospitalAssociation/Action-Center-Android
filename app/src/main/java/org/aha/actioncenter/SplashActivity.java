package org.aha.actioncenter;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import org.aha.actioncenter.utility.AHABusProvider;
import org.aha.actioncenter.utility.Utility;


public class SplashActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AHABusProvider.getInstance().register(this);
        setContentView(R.layout.splash_screen_view);

        boolean isValidLogin = !Utility.getInstance(getApplicationContext()).getLoginData("login").ahaid.isEmpty();

        Intent intent;
        if(isValidLogin){
            intent = new Intent(this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }
        else{
            intent = new Intent(this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }

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

}
