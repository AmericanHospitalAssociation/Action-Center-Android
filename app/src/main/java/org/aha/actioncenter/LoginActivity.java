package org.aha.actioncenter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import org.aha.actioncenter.utility.AHABusProvider;

/**
 * Created by markusmcgee on 4/23/15.
 */
public class LoginActivity extends Activity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.login_fragment_view);
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

        //SharedPreferences prefs = getApplicationContext().getSharedPreferences("login", Context.MODE_PRIVATE);
        //boolean isValidLogin = prefs.getBoolean("login", false);

        Intent intent;
        intent = new Intent(this, MainActivity.class);
        startActivity(intent);

    }
}