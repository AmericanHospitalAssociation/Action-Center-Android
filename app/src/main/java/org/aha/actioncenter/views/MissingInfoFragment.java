package org.aha.actioncenter.views;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;

import com.google.gson.reflect.TypeToken;

import org.aha.actioncenter.R;
import org.aha.actioncenter.models.OAMItem;
import org.aha.actioncenter.utility.AHABusProvider;
import org.aha.actioncenter.utility.Utility;

import java.lang.reflect.Type;

/**
 * Created by markusmcgee on 5/8/15.
 */
public class MissingInfoFragment extends Fragment implements View.OnClickListener{

    private RadioButton mr_btn;
    private RadioButton dr_btn;
    private RadioButton ms_btn;
    private RadioButton mrs_btn;

    private EditText phone_txt;

    private boolean isMr = false;
    private boolean isDr = false;
    private boolean isMs = false;
    private boolean isMrs = false;

    private Button update_btn;

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

        Type oamItemType = new TypeToken<OAMItem>() {
        }.getType();
        oamItem = Utility.getInstance(getActivity().getApplicationContext()).getLoginData();

        mr_btn = (RadioButton) view.findViewById(R.id.mr_btn);
        mr_btn.setOnClickListener(this);

        dr_btn = (RadioButton) view.findViewById(R.id.dr_btn);
        dr_btn.setOnClickListener(this);

        ms_btn = (RadioButton) view.findViewById(R.id.ms_btn);
        ms_btn.setOnClickListener(this);

        mrs_btn = (RadioButton) view.findViewById(R.id.mrs_btn);
        mrs_btn.setOnClickListener(this);

        update_btn = (Button) view.findViewById(R.id.update_btn);
        update_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                oamItem.phone = phone_txt.getText().toString();
                if(isMr)
                    oamItem.prefix = "Mr.";
                if(isDr)
                    oamItem.prefix = "Dr.";
                if(isMs)
                    oamItem.prefix = "Ms.";
                if(isMrs)
                    oamItem.prefix = "Mrs.";

                Utility.getInstance(getActivity().getApplicationContext()).saveLoginData("login", oamItem);
                getFragmentManager().popBackStack();
                //((MainActivity)getActivity()).selectItem();

            }
        });

        phone_txt = (EditText) view.findViewById(R.id.phone_txt);
        phone_txt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(!charSequence.toString().isEmpty()){
                    update_btn.setEnabled(true);
                }
                else{
                    update_btn.setEnabled(false);
                }
            }
            @Override
            public void afterTextChanged(Editable editable) {}
        });



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

        boolean checked = ((RadioButton) view).isChecked();

        // Check which radio button was clicked
        switch (view.getId()) {
            case R.id.mr_btn:
                if (checked){
                    isMr = true;
                    isDr = false;
                    isMs = false;
                    isMrs = false;
                }
                    break;
            case R.id.dr_btn:
                if (checked){
                    isMr = false;
                    isDr = true;
                    isMs = false;
                    isMrs = false;
                }

                    break;
            case R.id.ms_btn:
                if (checked){
                    isMr = false;
                    isDr = false;
                    isMs = true;
                    isMrs = false;
                }

                    break;
            case R.id.mrs_btn:
                if (checked){
                    isMr = false;
                    isDr = false;
                    isMs = false;
                    isMrs = true;
                }

                    break;
        }


    }
}
