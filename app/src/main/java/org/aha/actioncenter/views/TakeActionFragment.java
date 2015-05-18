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
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.aha.actioncenter.MainActivity;
import org.aha.actioncenter.R;
import org.aha.actioncenter.utility.AHABusProvider;

/**
 * Created by markusmcgee on 4/17/15.
 */
public class TakeActionFragment extends Fragment {

    private static final String TAG = "TakeActionFragment";
    protected TextView subject_txt = null;
    protected EditText message_txt = null;
    protected Button send_btn = null;

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
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.take_action_fragment_view, container, false);
        //OttoBus must be registered after inflate.inflate or app blows up.
        AHABusProvider.getInstance().register(this);

        ((MainActivity)getActivity()).setTitle("Personalize the Message");


        String mMessages = getArguments().getString("message");
        String mSubject = getArguments().getString("subject");
        String mGuidelines = getArguments().getString("guidelines");

        subject_txt = (TextView) view.findViewById(R.id.subject_txt);
        message_txt = (EditText) view.findViewById(R.id.message_txt);
        send_btn = (Button) view.findViewById(R.id.send_btn);

        subject_txt.setText(mSubject);
        message_txt.setText(mMessages);


        send_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getActivity().getApplicationContext(),"Take Action Send Click", Toast.LENGTH_SHORT).show();
            }
        });


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
