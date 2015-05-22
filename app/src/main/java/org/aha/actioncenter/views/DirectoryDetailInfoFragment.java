package org.aha.actioncenter.views;

import android.app.Fragment;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.telephony.PhoneNumberUtils;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.aha.actioncenter.R;
import org.aha.actioncenter.models.LegislatorItem;
import org.aha.actioncenter.utility.AHABusProvider;
import org.aha.actioncenter.utility.Utility;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by markusmcgee on 4/17/15.
 */
public class DirectoryDetailInfoFragment extends Fragment {

    private LegislatorItem item;
    private static final String TAG = "DirectoryDetailInfoFragment";

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

        item = Utility.getInstance(getActivity().getApplicationContext()).getLegislatorItem();


    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.legislator_detail_info_view, container, false);
        //OttoBus must be registered after inflate.inflate or app blows up.
        AHABusProvider.getInstance().register(this);

        LinearLayout content = (LinearLayout) view.findViewById(R.id.content);
        ImageView imageView = (ImageView) view.findViewById(R.id.image_view);
        TextView textView = new TextView(getActivity().getApplicationContext());
        textView.setText(item.displayName);
        textView.setTypeface(Typeface.DEFAULT_BOLD);
        textView.setTextColor(getResources().getColor(android.R.color.black));
        textView.setPadding(0, 15, 0, 0);
        content.addView(textView);

        new ImageLoadTask(item.photoUrl, imageView).execute();

        for (LegislatorItem.Sections section : item.sections) {

            TextView sectionLabel = new TextView(getActivity().getApplicationContext());
            sectionLabel.setText(section.name);
            sectionLabel.setTypeface(Typeface.DEFAULT_BOLD);
            sectionLabel.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
            sectionLabel.setPadding(0, 10, 0, 0);
            sectionLabel.setTextColor(getResources().getColor(android.R.color.black));
            content.addView(sectionLabel);

            for (final LegislatorItem.Properties property : section.properties) {

                TextView propertyName = new TextView(getActivity().getApplicationContext());
                TextView propertyValue = new TextView(getActivity().getApplicationContext());

                propertyName.setText(property.name);
                propertyName.setTypeface(Typeface.DEFAULT_BOLD);
                propertyName.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
                propertyName.setTextColor(getResources().getColor(android.R.color.black));
                propertyName.setPadding(0, 5, 0, 0);
                content.addView(propertyName);

                propertyValue.setText(property.value);

                if (property.name.equals("District Phone") || property.name.equals("Capitol Phone")) {

                    propertyValue.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent callIntent = new Intent(Intent.ACTION_CALL);
                            callIntent.setData(Uri.parse("tel:+" + property.value));
                            startActivity(callIntent);
                        }
                    });

                    propertyValue.setTextColor(getResources().getColor(R.color.aha_blue));

                }
                else {
                    propertyValue.setTextColor(getResources().getColor(android.R.color.black));
                }

                content.addView(propertyValue);
            }
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

    private class ImageLoadTask extends AsyncTask<Void, Void, Bitmap> {

        private String url;
        private ImageView imageView;

        public ImageLoadTask(String url, ImageView imageView) {
            this.url = url;
            this.imageView = imageView;
        }

        @Override
        protected Bitmap doInBackground(Void... params) {
            try {
                URL urlConnection = new URL(url);
                HttpURLConnection connection = (HttpURLConnection) urlConnection.openConnection();
                connection.setDoInput(true);
                connection.connect();
                InputStream input = connection.getInputStream();
                Bitmap myBitmap = BitmapFactory.decodeStream(input);
                return myBitmap;
            }
            catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Bitmap result) {
            super.onPostExecute(result);
            imageView.setImageBitmap(result);
        }

    }

}
