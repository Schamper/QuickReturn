package com.etiennelawlor.quickreturn.library.listeners;

import android.view.View;

import com.etiennelawlor.quickreturn.library.enums.QuickReturnType;

/**
 * Created by Schamper on 10/8/2014.
 */
public abstract class AbsQuickReturnOnScrollListener extends AbsBaseQuickReturnOnScrollListener {

    // region Member Variables
    protected int mMinFooterTranslation;
    protected int mMinHeaderTranslation;
    protected int mHeaderDiffTotal = 0;
    protected int mFooterDiffTotal = 0;
    protected int mMidHeader;
    protected int mMidFooter;
    protected View mHeader;
    protected View mFooter;
    protected QuickReturnType mQuickReturnType;
    // endregion

    public AbsQuickReturnOnScrollListener(QuickReturnType quickReturnType, View headerView, int headerTranslation, View footerView, int footerTranslation) {
        mQuickReturnType = quickReturnType;
        mHeader =  headerView;
        mMidHeader = -headerTranslation/2;
        mMinHeaderTranslation = headerTranslation;
        mFooter =  footerView;
        mMidFooter = footerTranslation/2;
        mMinFooterTranslation = footerTranslation;
    }

    protected void onScroll(int scrollY, int diff) {
        if (diff != 0) {
            boolean fall = false, isTwitter = false;
            switch (mQuickReturnType) {
                case TWITTER:
                    isTwitter = true;
                case BOTH:
                    fall = true;
                case HEADER:
                    mHeaderDiffTotal = calculateDiffTotal(diff, mHeaderDiffTotal, mMinHeaderTranslation, isTwitter, scrollY);
                    mHeader.setTranslationY(mHeaderDiffTotal);
                    if (!fall) break;
                case FOOTER:
                    mFooterDiffTotal = calculateDiffTotal(diff, mFooterDiffTotal, -mMinFooterTranslation, isTwitter, scrollY);
                    mFooter.setTranslationY(-mFooterDiffTotal);
                    break;
                default:
                    break;
            }
        }
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
}
