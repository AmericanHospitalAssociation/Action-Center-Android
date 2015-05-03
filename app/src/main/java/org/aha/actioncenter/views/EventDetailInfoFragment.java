package org.aha.actioncenter.views;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
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
public class EventDetailInfoFragment extends Fragment {

    protected TextView title_txt = null;
    protected TextView description_txt = null;
    protected TextView address_street_txt = null;
    protected TextView address_location_txt = null;

    /*
    public String clean_title = "";
    public String pretty_date = "";
    public String unix_date = "";
    public String meeting_start_time = "";
    public String meeting_end_time = "";
    public String meeting_location = "";
    public String link = "";
    public String guid = "";
    public String desc = "";
    public String location_stateprovince = "";
    public String location_city = "";
    public String location_street = "";
    public String location_name = "";
    public String location_zippostal = "";
    public String location_country = "";
    public String location_guidelines = "";
    public String location_contact = "";
    public String location_phone = "";
    public String location_map_url = "";
    public String location_weather_url = "";
    */


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
        View view = inflater.inflate(R.layout.calendar_item_detail_view, container, false);
        //OttoBus must be registered after inflate.inflate or app blows up.
        AHABusProvider.getInstance().register(this);

        Type eventItemType = new TypeToken<EventItem>(){}.getType();
        EventItem item = new Gson().fromJson(getArguments().getString("item"), eventItemType);

        title_txt = (TextView) view.findViewById(R.id.title_txt);
        address_street_txt = (TextView) view.findViewById(R.id.address_street_txt);
        address_location_txt = (TextView) view.findViewById(R.id.address_location_txt);
        description_txt = (TextView) view.findViewById(R.id.description_txt);

        title_txt.setText(item.clean_title);
        address_street_txt.setText(item.location_street);
        address_location_txt.setText(item.meeting_location);
        description_txt.setText(Html.fromHtml(item.desc));

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
