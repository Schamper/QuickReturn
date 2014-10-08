package com.etiennelawlor.quickreturn.library.listeners;

import android.view.View;
import android.widget.ScrollView;

import com.etiennelawlor.quickreturn.library.enums.QuickReturnType;
import com.etiennelawlor.quickreturn.library.views.NotifyingScrollView;

/**
 * Created by etiennelawlor on 7/11/14.
 */
public class QuickReturnScrollViewOnScrollChangedListener extends AbsQuickReturnOnScrollListener implements NotifyingScrollView.OnScrollChangedListener {

    // region Constructors
    public QuickReturnScrollViewOnScrollChangedListener(QuickReturnType quickReturnType, View headerView, int headerTranslation, View footerView, int footerTranslation){
        super(quickReturnType, headerView, headerTranslation, footerView, footerTranslation);
    }
    // endregion


    @Override
    public void onScrollChanged(ScrollView who, int l, int t, int oldl, int oldt) {
        int diff = oldt - t;
        onScroll(t, diff);
    }
}
