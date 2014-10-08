package com.etiennelawlor.quickreturn.library.listeners;

import android.content.Context;
import android.view.View;
import android.widget.AbsListView;

import com.etiennelawlor.quickreturn.library.R;
import com.etiennelawlor.quickreturn.library.enums.QuickReturnType;
import com.etiennelawlor.quickreturn.library.utils.QuickReturnUtils;

import java.util.ArrayList;

/**
 * Created by etiennelawlor on 7/14/14.
 */
public class SpeedyQuickReturnListViewOnScrollListener extends AbsSpeedyQuickReturnOnScrollListener implements AbsListView.OnScrollListener {

    // region Member Variables
    private ArrayList<View> mHeaderViews;
    private ArrayList<View> mFooterViews;
    private int mPrevScrollY = 0;
    // endregion

    // region Constructors
    public SpeedyQuickReturnListViewOnScrollListener(Context context, QuickReturnType quickReturnType, View headerView, View footerView) {
        super(context, quickReturnType, headerView, footerView);
    }

    public SpeedyQuickReturnListViewOnScrollListener(Context context, QuickReturnType quickReturnType, ArrayList<View> headerViews, ArrayList<View> footerViews) {
        super(context, quickReturnType, null, null);
        mHeaderViews = headerViews;
        mFooterViews = footerViews;
    }
    // endregion

    @Override
    public void onScrollStateChanged(AbsListView listView, int scrollState) {
      // apply extra listener first
      fireExtraOnScrollStateChangedListeners(listView, scrollState);
    }

    @Override
    public void onScroll(AbsListView listView, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        // apply extra listener first
        fireExtraOnScrollListeners(listView, firstVisibleItem, visibleItemCount, totalItemCount);

        int scrollY = QuickReturnUtils.getScrollY(listView);
        int diff = mPrevScrollY - scrollY;

        boolean fall = false;
        switch (mQuickReturnType) {
            case BOTH:
                fall = true;
            case HEADER:
                toggleSpeedyAnimation(diff, mHeader, mSlideHeaderDownAnimation, mSlideHeaderUpAnimation);
                if (!fall) break;
            case FOOTER:
                toggleSpeedyAnimation(diff, mFooter, mSlideFooterUpAnimation, mSlideFooterDownAnimation);
                break;
            case GOOGLE_PLUS:
                if (mHeaderViews != null) {
                    for (View view : mHeaderViews) {
                        toggleSpeedyAnimation(diff, view, mSlideHeaderDownAnimation, mSlideHeaderUpAnimation);
                    }
                }

                if (mFooterViews != null) {
                    for (View view : mFooterViews) {
                        int scrollThreshold = (Integer) view.getTag(R.id.scroll_threshold_key);
                        if ((diff < 0 && diff < -scrollThreshold) || (diff > 0 && diff > scrollThreshold)) {
                            toggleSpeedyAnimation(diff, view, mSlideFooterUpAnimation, mSlideFooterDownAnimation);
                        }
                    }
                }
                break;
        }

        mPrevScrollY = scrollY;
    }
}
