package org.aha.actioncenter.data;

import android.app.Activity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.CheckedTextView;
import android.widget.TextView;

import org.aha.actioncenter.MainActivity;
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
    public Activity activity;

    private NavigationItem navigationItem = null;

    public AHAExpandableListAdapter(Activity act, ArrayList<NavigationItem> navigationItems) {
        activity = act;
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
    public View getChildView(int groupPosition, final int childPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {
        navigationItem = (NavigationItem) getChild(groupPosition, childPosition);
        TextView text = null;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.listrow_details, null);
        }
        text = (TextView) convertView.findViewById(R.id.textView1);
        text.setText(navigationItem.name);

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
    public View getGroupView(int groupPosition, boolean isExpanded,
                             View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.listrow_group, null);
        }

        NavigationItem group = (NavigationItem) getGroup(groupPosition);
        ((CheckedTextView) convertView).setText(group.name);
        ((CheckedTextView) convertView).setChecked(isExpanded);
        return convertView;
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