package com.etiennelawlor.quickreturn.library.listeners;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.view.View;

import com.etiennelawlor.quickreturn.library.enums.QuickReturnState;
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
    private ObjectAnimator mAnimator;
    protected View mHeader;
    protected View mFooter;

    protected QuickReturnType mQuickReturnType;
    protected QuickReturnState mQuickReturnState = QuickReturnState.IDLE;

    private boolean mCanSlideInIdleScrollState = false;
    private int mAnimationDuration = 100;
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
            if (mAnimator != null) {
                mAnimator.cancel();
            }

            boolean fall = false;
            switch (mQuickReturnType) {
                case BOTH:
                    fall = true;
                case HEADER:
                    mCurrentHeaderTranslation = calculateTranslation(diff, scrollY, mCurrentHeaderTranslation, mMinHeaderTranslation);
                    translateView(mHeader, mCurrentHeaderTranslation, false);
                    if (!fall) break;
                case FOOTER:
                    mCurrentFooterTranslation = calculateTranslation(diff, scrollY, mCurrentFooterTranslation, -mMinFooterTranslation);
                    translateView(mFooter, -mCurrentFooterTranslation, false);
                    break;
                default:
                    break;
            }
        }
    }

    protected void handleOnScrollStateChanged(int scrollState, int scrollY) {
        if (mCanSlideInIdleScrollState && scrollState == SCROLL_STATE_IDLE && mQuickReturnState == QuickReturnState.IDLE) {
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
            translateView(view, 0, true);
            return 0;
        } else if (-currentTranslation < minTranslation && -currentTranslation >= midView && scrollY > minTranslation) {
            translateView(view, isFooter ? minTranslation : -minTranslation, true);
            return -minTranslation;
        }

        return currentTranslation;
    }

    private void translateView(View view, int translation, boolean animate) {
        if (animate) animateView(view, translation);
        else view.setTranslationY(translation);
    }

    protected void animateView(final View view, final int translation) {
        mAnimator = ObjectAnimator.ofFloat(view, "translationY", view.getTranslationY(), translation);
        mAnimator.setDuration(mAnimationDuration);
        mAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {
                mQuickReturnState = QuickReturnState.ANIMATING;
            }

            @Override
            public void onAnimationEnd(Animator animator) {
                mQuickReturnState = QuickReturnState.IDLE;
            }

            @Override
            public void onAnimationCancel(Animator animator) {
                int currentTranslation = Math.round((Float) mAnimator.getAnimatedValue());
                // Pointers would be useful now
                if (view.equals(mHeader)) {
                    mCurrentHeaderTranslation = currentTranslation;
                } else {
                    mCurrentFooterTranslation = currentTranslation;
                }
                mAnimator.end();
                mAnimator = null;
            }

            @Override
            public void onAnimationRepeat(Animator animator) {}
        });
        mAnimator.start();
    }
    // endregion

    // region Settings
    public void setCanSlideInIdleScrollState(boolean canSlideInIdleScrollState){
        mCanSlideInIdleScrollState = canSlideInIdleScrollState;
    }

    public void setAnimationDuration(int duration) {
        mAnimationDuration = duration;
    }

    public void setDelayOffset(int offset) {
        mDelayOffset = offset;
    }
    // endregion

    // region Utility Methods
    public boolean isHeaderFullyHidden() {
        return mCurrentHeaderTranslation == mMinHeaderTranslation;
    }

    public boolean isFooterFullyHidden() {
        return mCurrentFooterTranslation == mMinFooterTranslation;
    }

    public boolean isHeaderFullyVisible() {
        return mCurrentHeaderTranslation == 0;
    }

    public boolean isFooterFullyVisible() {
        return mCurrentFooterTranslation == 0;
    }

    public void showHeader() {
        showHeader(false);
    }

    public void showHeader(boolean animate) {
        translateView(mHeader, 0, animate);
        mCurrentHeaderTranslation = 0;
    }

    public void hideHeader() {
        hideHeader(false);
    }

    public void hideHeader(boolean animate) {
        translateView(mHeader, mMinHeaderTranslation, animate);
        mCurrentHeaderTranslation = mMinHeaderTranslation;
    }

    public void showFooter() {
        showFooter(false);
    }

    public void showFooter(boolean animate) {
        translateView(mFooter, 0, animate);
        mCurrentFooterTranslation = 0;
    }

    public void hideFooter() {
        hideFooter(false);
    }

    public void hideFooter(boolean animate) {
        translateView(mFooter, mMinFooterTranslation, animate);
        mCurrentFooterTranslation = mMinFooterTranslation;
    }
    // endregion
}
