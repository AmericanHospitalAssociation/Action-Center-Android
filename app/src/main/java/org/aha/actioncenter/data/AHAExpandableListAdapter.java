package org.aha.actioncenter.data;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.CheckedTextView;
import android.widget.ImageView;
import android.widget.TextView;

import org.aha.actioncenter.R;
import org.aha.actioncenter.models.NavigationItem;

import java.util.ArrayList;

/**
 * Created by markusmcgee on 4/20/15.
 */
public class AHAExpandableListAdapter extends BaseExpandableListAdapter {

    private static final String TAG = "AHAExpandableListAdapter";
    private final ArrayList<NavigationItem> navigationItems;
    public LayoutInflater inflater;
    public Activity mActivity;

    public AHAExpandableListAdapter(Activity act, ArrayList<NavigationItem> navigationItems) {
        mActivity = act;
        this.navigationItems = navigationItems;
        inflater = act.getLayoutInflater();
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return navigationItems.get(groupPosition).subnav.get(childPosition);
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return 0;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded,
                             View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.listrow_details, null);
        }

        NavigationItem item = (NavigationItem) getGroup(groupPosition);

        CheckedTextView navigation_txt =  (CheckedTextView)convertView.findViewById(R.id.navigation_txt);
        navigation_txt.setText(item.name);
        navigation_txt.setChecked(isExpanded);


        if(!item.icon.isEmpty()){
            ImageView img = (ImageView) convertView.findViewById(R.id.drawer_item_icon);
            img.setImageResource(mActivity.getResources().getIdentifier(item.icon, "drawable", mActivity.getPackageName()));
        }


        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, final int childPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {
        NavigationItem item = (NavigationItem) getChild(groupPosition, childPosition);
        TextView text = null;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.listrow_group, null);
        }
        text = (TextView) convertView.findViewById(R.id.navigation_txt);
        text.setText(item.name);

        return convertView;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        if(navigationItems.get(groupPosition).subnav == null)
            return 0;

        return navigationItems.get(groupPosition).subnav.size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return navigationItems.get(groupPosition);
    }

    @Override
    public int getGroupCount() {
        return navigationItems.size();
    }

    @Override
    public void onGroupCollapsed(int groupPosition) {
        super.onGroupCollapsed(groupPosition);
    }

    @Override
    public void onGroupExpanded(int groupPosition) {
        super.onGroupExpanded(groupPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return 0;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}