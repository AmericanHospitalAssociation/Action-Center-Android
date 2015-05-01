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
import android.widget.TextView;

import org.aha.actioncenter.R;
import org.aha.actioncenter.utility.AHABusProvider;

/**
 * Created by markusmcgee on 4/17/15.
 */
public class ProfileDetailInfoFragment extends Fragment {

    protected TextView title_txt = null;
    protected TextView description_txt = null;
    protected TextView name_txt = null;

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater = getActivity().getMenuInflater();
        inflater.inflate(R.menu.detail_menu, menu);
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
        View view = inflater.inflate(R.layout.feed_item_detail_view, container, false);
        //OttoBus must be registered after inflate.inflate or app blows up.
        AHABusProvider.getInstance().register(this);

        /*
        Type campaignItemType = new TypeToken<DirectoryItem>(){}.getType();
        DirectoryItem item = new Gson().fromJson(getArguments().getString("item"), campaignItemType);


        title_txt = (TextView) view.findViewById(R.id.title_txt);
        description_txt = (TextView) view.findViewById(R.id.description_txt);
        name_txt = (TextView) view.findViewById(R.id.name_txt);

        title_txt.setText(item.title);
        description_txt.setText(Html.fromHtml(item.description));
        name_txt.setText(item.name);
        */

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
