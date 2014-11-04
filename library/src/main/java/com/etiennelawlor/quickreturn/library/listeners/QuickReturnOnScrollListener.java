package com.etiennelawlor.quickreturn.library.listeners;

import android.animation.ObjectAnimator;
import android.view.View;

import com.etiennelawlor.quickreturn.library.enums.QuickReturnType;

/**
 * Created by Schamper on 10/8/2014.
 */
public class QuickReturnOnScrollListener extends OnScrollListenerWrapper {

    // region Member Variables
    private int mCurrentHeaderTranslation = 0;
    private int mCurrentFooterTranslation = 0;
    private int mMinFooterTranslation;
    private int mMinHeaderTranslation;
    private int mMidHeader;
    private int mMidFooter;
    protected View mHeader;
    protected View mFooter;

    protected QuickReturnType mQuickReturnType;

    private boolean mCanSlideInIdleScrollState = false;
    private int mDelayOffset = 0;
    // endregion


    // region Constructor
    public QuickReturnOnScrollListener(QuickReturnType quickReturnType, View headerView, int headerTranslation, View footerView, int footerTranslation) {
        mQuickReturnType = quickReturnType;

        mHeader =  headerView;
        mMidHeader = -headerTranslation / 2;
        mMinHeaderTranslation = headerTranslation;

        mFooter =  footerView;
        mMidFooter = footerTranslation / 2;
        mMinFooterTranslation = footerTranslation;
    }
    // endregion

    // region Handlers
    protected void handleOnScrollChanged(int diff, int scrollY) {
        if (diff != 0) {
            boolean fall = false;
            switch (mQuickReturnType) {
                case BOTH:
                    fall = true;
                case HEADER:
                    mCurrentHeaderTranslation = calculateTranslation(diff, scrollY, mCurrentHeaderTranslation, mMinHeaderTranslation);
                    mHeader.setTranslationY(mCurrentHeaderTranslation);
                    if (!fall) break;
                case FOOTER:
                    mCurrentFooterTranslation = calculateTranslation(diff, scrollY, mCurrentFooterTranslation, -mMinFooterTranslation);
                    mFooter.setTranslationY(-mCurrentFooterTranslation);
                    break;
                default:
                    break;
            }
        }
    }

    protected void handleOnScrollStateChanged(int scrollState, int scrollY) {
        if (mCanSlideInIdleScrollState && scrollState == SCROLL_STATE_IDLE) {
            boolean fall = false;
            switch (mQuickReturnType) {
                case BOTH:
                    fall = true;
                case HEADER:
                    mCurrentHeaderTranslation = snapView(mHeader, mCurrentHeaderTranslation, mMidHeader, scrollY, mMinHeaderTranslation, false);
                    if (!fall) break;
                case FOOTER:
                    mCurrentFooterTranslation = snapView(mFooter, mCurrentFooterTranslation, mMidFooter, scrollY, mMinFooterTranslation, true);
                    break;
                default:
                    break;
            }
        }
    }
    // endregion

    // region Utility Methods
    public boolean isHeaderHidden() {
        return mCurrentHeaderTranslation == mMinHeaderTranslation;
    }

    public boolean isFooterHidden() {
        return mCurrentFooterTranslation == mMinFooterTranslation;
    }

    public void showHeader() {
        animateView(mHeader, 0);
        mCurrentHeaderTranslation = 0;
    }

    public void hideHeader() {
        animateView(mHeader, mMinHeaderTranslation);
        mCurrentHeaderTranslation = mMinHeaderTranslation;
    }

    public void showFooter() {
        animateView(mFooter, 0);
        mCurrentFooterTranslation = 0;
    }

    public void hideFooter() {
        animateView(mFooter, mMinFooterTranslation);
        mCurrentFooterTranslation = mMinFooterTranslation;
    }
    // endregion

    // region Settings
    public void setCanSlideInIdleScrollState(boolean canSlideInIdleScrollState){
        mCanSlideInIdleScrollState = canSlideInIdleScrollState;
    }

    public void setDelayOffset(int offset) {
        mDelayOffset = offset;
    }
    // endregion

    // region Logic
    protected int calculateTranslation(int scrollDiff, int scrollY, int currentTranslation, int minTranslation) {
        if (scrollDiff < 0) {
            if (mDelayOffset > 0 && scrollY <= mDelayOffset) {
                return currentTranslation;
            }
            return Math.max(currentTranslation + scrollDiff, minTranslation);
        } else {
            return Math.min(Math.max(currentTranslation + scrollDiff, minTranslation), 0);
        }
    }

    private int snapView(View view, int currentTranslation, int midView, int scrollY, int minTranslation, boolean isFooter) {
        minTranslation = (isFooter ? minTranslation : -minTranslation);
        if ((-currentTranslation > 0 && -currentTranslation < midView) || scrollY <= minTranslation) {
            animateView(view, 0);
            return 0;
        } else if (-currentTranslation < minTranslation && -currentTranslation >= midView && scrollY > minTranslation) {
            animateView(view, isFooter ? minTranslation : -minTranslation);
            return -minTranslation;
        }

        return currentTranslation;
    }

    protected void animateView(View view, int translation) {
        ObjectAnimator anim = ObjectAnimator.ofFloat(view, "translationY", view.getTranslationY(), translation);
        anim.setDuration(100);
        anim.start();
    }
    // endregion
}
