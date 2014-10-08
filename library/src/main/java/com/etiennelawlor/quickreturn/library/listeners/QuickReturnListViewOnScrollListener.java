package com.etiennelawlor.quickreturn.library.listeners;

import android.animation.ObjectAnimator;
import android.view.View;
import android.widget.AbsListView;

import com.etiennelawlor.quickreturn.library.enums.QuickReturnType;
import com.etiennelawlor.quickreturn.library.utils.QuickReturnUtils;

/**
 * Created by etiennelawlor on 7/10/14.
 */
public class QuickReturnListViewOnScrollListener extends AbsQuickReturnOnScrollListener implements AbsListView.OnScrollListener {

    // region Member Variables
    private int mPrevScrollY = 0;
    private boolean mCanSlideInIdleScrollState = false;
    // endregion

    // region Constructors
    public QuickReturnListViewOnScrollListener(QuickReturnType quickReturnType, View headerView, int headerTranslation, View footerView, int footerTranslation) {
        super(quickReturnType, headerView, headerTranslation, footerView, footerTranslation);
    }
    // endregion

    @Override
    public void onScrollStateChanged(AbsListView listView, int scrollState) {
        // apply another list' s on scroll listener
        fireExtraOnScrollStateChangedListeners(listView, scrollState);

        if (scrollState == SCROLL_STATE_IDLE && mCanSlideInIdleScrollState) {
            boolean fall = false;
            // TODO: if scrollY == 0 always show header/footer
            switch (mQuickReturnType) {
                case BOTH:
                case TWITTER:
                    fall = true;
                case HEADER:
                    if (-mHeaderDiffTotal > 0 && -mHeaderDiffTotal < mMidHeader) {
                        animateSnap(mHeader, 0);
                        mHeaderDiffTotal = 0;
                    } else if (-mHeaderDiffTotal < -mMinHeaderTranslation && -mHeaderDiffTotal >= mMidHeader) {
                        animateSnap(mHeader, mMinHeaderTranslation);
                        mHeaderDiffTotal = mMinHeaderTranslation;
                    }
                    if (!fall) break;
                case FOOTER:
                    if (-mFooterDiffTotal > 0 && -mFooterDiffTotal < mMidFooter) { // slide up
                        animateSnap(mFooter, 0);
                        mFooterDiffTotal = 0;
                    } else if (-mFooterDiffTotal < mMinFooterTranslation && -mFooterDiffTotal >= mMidFooter) { // slide down
                        animateSnap(mFooter, mMinFooterTranslation);
                        mFooterDiffTotal = -mMinFooterTranslation;
                    }
                    break;
                default:
                    break;
            }
        }
    }

    @Override
    public void onScroll(AbsListView listView, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        // apply extra on scroll listener
        fireExtraOnScrollListeners(listView, firstVisibleItem, visibleItemCount, totalItemCount);

        int scrollY = QuickReturnUtils.getScrollY(listView);
        int diff = mPrevScrollY - scrollY;
        onScroll(scrollY, diff);

        mPrevScrollY = scrollY;
    }

    public void setCanSlideInIdleScrollState(boolean canSlideInIdleScrollState){
        mCanSlideInIdleScrollState = canSlideInIdleScrollState;
    }

    private void animateSnap(View view, int translation) {
        ObjectAnimator anim = ObjectAnimator.ofFloat(view, "translationY", view.getTranslationY(), translation);
        anim.setDuration(100);
        anim.start();
    }
}
