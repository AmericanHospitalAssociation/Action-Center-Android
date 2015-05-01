package org.aha.actioncenter.views;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.aha.actioncenter.R;
import org.aha.actioncenter.models.EventItem;
import org.aha.actioncenter.utility.AHABusProvider;

import java.lang.reflect.Type;

/**
 * Created by markusmcgee on 4/17/15.
 */
public class DirectoryDetailInfoFragment extends Fragment {

    protected TextView title_txt = null;
    protected TextView action_from_txt = null;
    protected TextView action_needed_txt = null;
    protected TextView when_c_txt = null;
    protected TextView why_txt = null;
    protected TextView long_description_txt = null;
    protected TextView resource_uri_txt = null;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.event_item_detail_view, container, false);
        //OttoBus must be registered after inflate.inflate or app blows up.
        AHABusProvider.getInstance().register(this);

        Type eventItemType = new TypeToken<EventItem>(){}.getType();
        EventItem item = new Gson().fromJson(getArguments().getString("item"), eventItemType);

        TextView  clean_title = (TextView) view.findViewById(R.id.clean_title);
        TextView pretty_date = (TextView) view.findViewById(R.id.pretty_date);
        TextView meeting_location = (TextView) view.findViewById(R.id.meeting_location);
        TextView link = (TextView) view.findViewById(R.id.link);

        clean_title.setText(item.clean_title);
        pretty_date.setText(item.pretty_date);
        meeting_location.setText(item.meeting_location);
        link.setText(item.link);

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
