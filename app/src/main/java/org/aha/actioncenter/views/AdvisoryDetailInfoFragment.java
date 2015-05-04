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
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.aha.actioncenter.R;
import org.aha.actioncenter.models.FeedItem;
import org.aha.actioncenter.service.PdfDownloadAsyncTask;
import org.aha.actioncenter.utility.AHABusProvider;
import org.aha.actioncenter.utility.Utility;

import java.lang.reflect.Type;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by markusmcgee on 4/17/15.
 */
public class AdvisoryDetailInfoFragment extends Fragment {

    protected TextView title_txt = null;
    protected TextView date_txt = null;
    protected TextView description_txt = null;
    protected Button read_more_btn = null;

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
        View view = inflater.inflate(R.layout.advisory_item_detail_view, container, false);
        //OttoBus must be registered after inflate.inflate or app blows up.
        AHABusProvider.getInstance().register(this);

        Type feedItemType = new TypeToken<FeedItem>(){}.getType();
        final FeedItem item = new Gson().fromJson(getArguments().getString("item"), feedItemType);

        title_txt = (TextView) view.findViewById(R.id.title_txt);
        description_txt = (TextView) view.findViewById(R.id.description_txt);
        date_txt = (TextView) view.findViewById(R.id.date_txt);

        title_txt.setText(item.Title);
        description_txt.setText(item.Description);
        date_txt.setText(item.Date);

        if (!item.ResourceURI.isEmpty()) {
            read_more_btn = (Button) view.findViewById(R.id.read_more_btn);
            read_more_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(Utility.getInstance().isNetworkAvailable(getActivity())) {
                        try {
                            new PdfDownloadAsyncTask(new URL(item.ResourceURI), getActivity().getApplicationContext(), getActivity()).execute();
                        }
                        catch (MalformedURLException e) {
                            e.printStackTrace();
                        }
                    }
                }
            });
            read_more_btn.setVisibility(View.VISIBLE);
        }

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
