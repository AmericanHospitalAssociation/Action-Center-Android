package org.aha.actioncenter.views;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckedTextView;

import com.google.gson.reflect.TypeToken;

import org.aha.actioncenter.R;
import org.aha.actioncenter.models.OAMItem;
import org.aha.actioncenter.utility.AHABusProvider;
import org.aha.actioncenter.utility.Utility;

import java.lang.reflect.Type;

/**
 * Created by markusmcgee on 5/8/15.
 */
public class MissingInfoFragment extends Fragment implements View.OnClickListener {

    private CheckedTextView mr_txt;
    private CheckedTextView dr_txt;
    private CheckedTextView ms_txt;
    private CheckedTextView mrs_txt;

    OAMItem oamItem = null;

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        inflater = getActivity().getMenuInflater();
        inflater.inflate(R.menu.detail_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.close_detail_action) {
            getFragmentManager().popBackStack();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        oamItem = Utility.getInstance(getActivity().getApplicationContext()).getLoginData();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.missing_user_info_view, container, false);
        //OttoBus must be registered after inflate.inflate or app blows up.
        AHABusProvider.getInstance().register(this);

        Type oamItemType = new TypeToken<OAMItem>(){}.getType();
        oamItem = Utility.getInstance(getActivity().getApplicationContext()).getLoginData();

        mr_txt = (CheckedTextView) view.findViewById(R.id.mr_txt);
        mr_txt.setOnClickListener(this);

        dr_txt = (CheckedTextView) view.findViewById(R.id.dr_txt);
        dr_txt.setOnClickListener(this);

        ms_txt = (CheckedTextView) view.findViewById(R.id.ms_txt);
        ms_txt.setOnClickListener(this);

        mrs_txt = (CheckedTextView) view.findViewById(R.id.mrs_txt);
        mrs_txt.setOnClickListener(this);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onClick(View view) {

        if(view == mr_txt){

        }

        if(view == dr_txt){

        }

        if(view == ms_txt){

        }

        if(view == mrs_txt){

        }
    }
}
