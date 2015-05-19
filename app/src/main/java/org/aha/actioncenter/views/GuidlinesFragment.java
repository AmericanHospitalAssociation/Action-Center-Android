package org.aha.actioncenter.views;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import org.aha.actioncenter.R;
import org.aha.actioncenter.models.CampaignUserItem;
import org.aha.actioncenter.utility.Utility;

import java.util.List;

/**
 * Created by markusmcgee on 5/18/15.
 */
public class GuidlinesFragment extends Fragment {

    private Button btn_ok = null;
    private TextView recipient_txt = null;
    private TextView guidlines_txt = null;
    private Context mContext = null;
    private List<CampaignUserItem> mList;
    private Bundle bundle;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        mContext = getActivity().getApplicationContext();
        mList = Utility.getInstance(mContext).getDirectoryData();

        bundle = getArguments();

        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.guidlines_info_view, container, false);

        btn_ok = (Button)view.findViewById(R.id.btn_ok);
        recipient_txt = (TextView)view.findViewById(R.id.recipient_txt);
        guidlines_txt = (TextView)view.findViewById(R.id.guidlines_txt);





        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getFragmentManager().popBackStack();
            }
        });
        return view;
    }
}
