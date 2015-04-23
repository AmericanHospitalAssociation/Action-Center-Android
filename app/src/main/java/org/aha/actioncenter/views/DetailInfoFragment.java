package org.aha.actioncenter.views;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBarActivity;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toolbar;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.aha.actioncenter.R;
import org.aha.actioncenter.models.FeedItem;
import org.aha.actioncenter.utility.AHABusProvider;

import java.lang.reflect.Type;

/**
 * Created by markusmcgee on 4/17/15.
 */
public class DetailInfoFragment extends Fragment {

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
        View view = inflater.inflate(R.layout.feed_item_detail_view, container, false);
        //OttoBus must be registered after inflate.inflate or app blows up.
        AHABusProvider.getInstance().register(this);

        Type feedItemType = new TypeToken<FeedItem>(){}.getType();
        FeedItem item = new Gson().fromJson(getArguments().getString("item"), feedItemType);

        title_txt = (TextView) view.findViewById(R.id.title_txt);
        //action_from_txt = (TextView) view.findViewById(R.id.action_from_txt);
        //action_needed_txt = (TextView) view.findViewById(R.id.action_needed_txt);
        //when_c_txt = (TextView) view.findViewById(R.id.when_c_txt);
        //why_txt = (TextView) view.findViewById(R.id.why_txt);
        long_description_txt = (TextView) view.findViewById(R.id.long_description_txt);
        resource_uri_txt = (TextView) view.findViewById(R.id.resource_uri_txt);


        title_txt.setText(item.Title);
        //action_from_txt.setText(item.ActionFrom);
        //action_needed_txt.setText(item.ActionNeeded);
        //when_c_txt.setText(item.When_c);
        //why_txt.setText(item.Why);
        long_description_txt.setText(Html.fromHtml(item.Long_Description));
        resource_uri_txt = (TextView) view.findViewById(R.id.resource_uri_txt);

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
