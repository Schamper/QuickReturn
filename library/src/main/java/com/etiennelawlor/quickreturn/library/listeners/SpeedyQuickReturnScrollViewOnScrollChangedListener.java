package com.etiennelawlor.quickreturn.library.listeners;

import android.content.Context;
import android.view.View;
import android.widget.ScrollView;

import com.etiennelawlor.quickreturn.library.enums.QuickReturnType;
import com.etiennelawlor.quickreturn.library.views.NotifyingScrollView;


/**
 * Created by etiennelawlor on 7/14/14.
 */
public class SpeedyQuickReturnScrollViewOnScrollChangedListener extends AbsSpeedyQuickReturnOnScrollListener implements NotifyingScrollView.OnScrollChangedListener {

    // region Constructors
    public SpeedyQuickReturnScrollViewOnScrollChangedListener(Context context, QuickReturnType quickReturnType, View headerView, View footerView) {
        super(context, quickReturnType, headerView, footerView);
    }
    // endregion

    @Override
    public void onScrollChanged(ScrollView who, int l, int t, int oldl, int oldt) {
        int diff = oldt - t;

        boolean fall = false;
        switch (mQuickReturnType){
            case BOTH:
                fall = true;
            case HEADER:
                toggleSpeedyAnimation(diff, mHeader, mSlideHeaderDownAnimation, mSlideHeaderUpAnimation);
                if (!fall) break;
            case FOOTER:
                toggleSpeedyAnimation(diff, mFooter, mSlideFooterUpAnimation, mSlideFooterDownAnimation);
                break;
        }
    }
}
