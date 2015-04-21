package org.aha.actioncenter.views;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.aha.actioncenter.R;
import org.aha.actioncenter.utility.AHABusProvider;

/**
 * Created by markusmcgee on 4/20/15.
 */
public class LoginViewFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.login_fragment_view, container, false);
        AHABusProvider.getInstance().register(this);



        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        AHABusProvider.getInstance().register(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        AHABusProvider.getInstance().unregister(this);
    }

}