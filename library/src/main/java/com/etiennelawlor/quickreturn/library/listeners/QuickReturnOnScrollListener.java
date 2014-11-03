package com.etiennelawlor.quickreturn.library.listeners;

import android.animation.ObjectAnimator;
import android.view.View;

import com.etiennelawlor.quickreturn.library.enums.QuickReturnType;

/**
 * Created by Schamper on 10/8/2014.
 */
public class QuickReturnOnScrollListener extends OnScrollListenerWrapper {

    // region Member Variables
    private int mMinFooterTranslation;
    private int mMinHeaderTranslation;
    private int mHeaderDiffTotal = 0;
    private int mFooterDiffTotal = 0;
    private int mMidHeader;
    private int mMidFooter;
    protected View mHeader;
    protected View mFooter;

    protected QuickReturnType mQuickReturnType;
    private boolean mCanSlideInIdleScrollState = false;
    // endregion


    // region Constructor
    public QuickReturnOnScrollListener(QuickReturnType quickReturnType, View headerView, int headerTranslation, View footerView, int footerTranslation) {
        mQuickReturnType = quickReturnType;
        mHeader =  headerView;
        mMidHeader = -headerTranslation/2;
        mMinHeaderTranslation = headerTranslation;
        mFooter =  footerView;
        mMidFooter = footerTranslation/2;
        mMinFooterTranslation = footerTranslation;
    }
    // endregion

    // region Handlers
    protected void handleOnScrollChanged(int oldY, int newY) {
        int diff = oldY - newY;
        if (diff != 0) {
            boolean fall = false, isTwitter = false;
            switch (mQuickReturnType) {
                case TWITTER:
                    isTwitter = true;
                case BOTH:
                    fall = true;
                case HEADER:
                    mHeaderDiffTotal = calculateDiffTotal(diff, mHeaderDiffTotal, mMinHeaderTranslation, isTwitter, newY);
                    mHeader.setTranslationY(mHeaderDiffTotal);
                    if (!fall) break;
                case FOOTER:
                    mFooterDiffTotal = calculateDiffTotal(diff, mFooterDiffTotal, -mMinFooterTranslation, isTwitter, newY);
                    mFooter.setTranslationY(-mFooterDiffTotal);
                    break;
                default:
                    break;
            }
        }
    }

    protected void handleOnScrollStateChanged(int scrollState) {
        if (scrollState == SCROLL_STATE_IDLE && mCanSlideInIdleScrollState) {
            boolean fall = false;
            // TODO: if scrollY == 0 always show header/footer
            switch (mQuickReturnType) {
                case BOTH:
                case TWITTER:
                    fall = true;
                case HEADER:
                    mHeaderDiffTotal = snapView(mHeader, mHeaderDiffTotal, mMidHeader, mMinHeaderTranslation, false);
                    if (!fall) break;
                case FOOTER:
                    mFooterDiffTotal = snapView(mFooter, mFooterDiffTotal, mMidFooter, mMinFooterTranslation, true);
                    break;
                default:
                    break;
            }
        }
    }
    // endregion

    // region Utility Methods
    public boolean isHeaderHidden() {
        return mHeaderDiffTotal == mMinHeaderTranslation;
    }
    
    public boolean isFooterHidden() {
        return mFooterDiffTotal == mMinFooterTranslation;
    }
    
    public void showHeader() {
        animateView(mHeader, 0);
        mHeaderDiffTotal = 0;
    }
    
    public void hideHeader() {
        animateView(mHeader, mMinHeaderTranslation);
        mHeaderDiffTotal = mMinHeaderTranslation;
    }
    
    public void showFooter() {
        animateView(mFooter, 0);
        mFooterDiffTotal = 0;
    }
    
    public void hideFooter() {
        animateView(mFooter, mMinFooterTranslation);
        mFooterDiffTotal = mMinFooterTranslation;
    }
    // endregion

    // region Settings
    public void setCanSlideInIdleScrollState(boolean canSlideInIdleScrollState){
        mCanSlideInIdleScrollState = canSlideInIdleScrollState;
    }
    // endregion

    // region Logic
    private int snapView(View view, int diffTotal, int midView, int translation, boolean isFooter) {
        if (-diffTotal > 0 && -diffTotal < midView) {
            animateView(view, 0);
            return 0;
        } else if (-diffTotal < (isFooter ? translation : -translation) && -diffTotal >= midView) {
            animateView(view, translation);
            return isFooter ? -translation : translation;
        }

        return diffTotal;
    }

    protected int calculateDiffTotal(int diff, int currentDiffTotal, int minTranslation,
                                   boolean isTwitter, int scrollY) {
        if (diff < 0) {
            if (isTwitter) {
                if (scrollY <= -minTranslation) {
                    return currentDiffTotal;
                }
            }
            return Math.max(currentDiffTotal + diff, minTranslation);
        } else {
            return Math.min(Math.max(currentDiffTotal + diff, minTranslation), 0);
        }
    }
    
    protected void animateView(View view, int translation) {
        ObjectAnimator anim = ObjectAnimator.ofFloat(view, "translationY", view.getTranslationY(), translation);
        anim.setDuration(100);
        anim.start();
    }
    // endregion
}
