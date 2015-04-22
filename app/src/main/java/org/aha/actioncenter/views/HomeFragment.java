package org.aha.actioncenter.views;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.aha.actioncenter.R;
import org.aha.actioncenter.utility.AHABusProvider;

/**
 * Created by markusmcgee on 4/22/15.
 */
public class HomeFragment extends Fragment {

    private Context mContext = null;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        mContext = getActivity().getApplicationContext();
        View view = inflater.inflate(R.layout.home_view, container, false);
        return view;
    }

    @Override
    public void onPause() {
        AHABusProvider.getInstance().unregister(this);
        super.onPause();
    }

    @Override
    public void onResume() {
        AHABusProvider.getInstance().register(this);
        super.onResume();
    }
}
