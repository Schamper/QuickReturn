package com.etiennelawlor.quickreturn.library.utils;

import android.content.Context;
import android.util.DisplayMetrics;
import android.util.SparseIntArray;
import android.util.TypedValue;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.AbsListView;

import java.util.Dictionary;
import java.util.Hashtable;

/**
 * Created by etiennelawlor on 6/28/14.
 */
public class QuickReturnUtils {

    private final AbsListView mListView;
    private static TypedValue sTypedValue = new TypedValue();
    private static int sActionBarHeight;
    private SparseIntArray mPositions;
    private Dictionary<Integer, Integer> sListViewItemHeights = new Hashtable<Integer, Integer>();

    public QuickReturnUtils(AbsListView listView) {
        mListView = listView;
    }

    public boolean isAtTop() {
        return mListView.canScrollVertically(-1);
    }

    public int getScrollY(int firstVisiblePosition) {
        View c = mListView.getChildAt(0);

        if (c == null) {
            return 0;
        }

        int scrollY = -c.getTop();

        sListViewItemHeights.put(firstVisiblePosition, c.getHeight());

        if (scrollY < 0) {
            scrollY = 0;
        }

        for (int i = 0; i < firstVisiblePosition; ++i) {
            if (sListViewItemHeights.get(i) != null) { // (this is a sanity check)
                scrollY += sListViewItemHeights.get(i); //add all heights of the views that are gone
            }
        }

        return scrollY;
    }

    public int getIncrementalOffset(final int firstVisiblePosition, final int visibleItemCount) {
        // Remember previous positions, if any
        SparseIntArray previousPositions = mPositions;

        // Store new positions
        mPositions = new SparseIntArray();
        for (int i = 0; i < visibleItemCount; i++) {
            mPositions.put(firstVisiblePosition + i, mListView.getChildAt(i).getTop());
        }

        if (previousPositions != null) {
            // Find position which exists in both mPositions and previousPositions, then return the difference
            // of the new and old Y values.
            for (int i = 0; i < previousPositions.size(); i++) {
                int position = previousPositions.keyAt(i);
                int previousTop = previousPositions.get(position);
                int newTop = mPositions.get(position, Integer.MAX_VALUE);
                if (newTop != Integer.MAX_VALUE) {
                    return newTop - previousTop;
                }
            }
        }

        return 0; // No view's position was in both previousPositions and mPositions
    }

    public static int dp2px(Context context, int dp) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();

        DisplayMetrics displaymetrics = new DisplayMetrics();
        display.getMetrics(displaymetrics);

        return (int) (dp * displaymetrics.density + 0.5f);
    }

    public static int px2dp(Context context, int px) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();

        DisplayMetrics displaymetrics = new DisplayMetrics();
        display.getMetrics(displaymetrics);

        return (int) (px / displaymetrics.density + 0.5f);
    }

    public static int getActionBarHeight(Context context) {
        if (sActionBarHeight != 0) {
            return sActionBarHeight;
        }

        context.getTheme().resolveAttribute(android.R.attr.actionBarSize, sTypedValue, true);
        sActionBarHeight = TypedValue.complexToDimensionPixelSize(sTypedValue.data, context.getResources().getDisplayMetrics());
        return sActionBarHeight;
    }
}
