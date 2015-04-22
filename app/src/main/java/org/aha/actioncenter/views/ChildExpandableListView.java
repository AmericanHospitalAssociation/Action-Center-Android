package org.aha.actioncenter.views;

import android.content.Context;
import android.widget.ExpandableListView;

/**
 * Created by markusmcgee on 4/21/15.
 */
public class ChildExpandableListView extends ExpandableListView {
    public ChildExpandableListView(Context context) {
        super(context);
    }

    @Override
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        widthMeasureSpec = MeasureSpec.makeMeasureSpec(1000, MeasureSpec.AT_MOST);
        heightMeasureSpec = MeasureSpec.makeMeasureSpec(1000, MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    //from http://stackoverflow.com/questions/19298155/issue-with-expanding-multi-level-expandablelistview
    @Override
    protected void onDetachedFromWindow() {
        try {
            super.onDetachedFromWindow();
        } catch (IllegalArgumentException e) {
            // TODO: Workaround for http://code.google.com/p/android/issues/detail?id=22751
        }
    }
}
