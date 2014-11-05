package com.etiennelawlor.quickreturn.library.listeners;

import android.content.Context;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.etiennelawlor.quickreturn.library.R;
import com.etiennelawlor.quickreturn.library.enums.QuickReturnType;

import java.util.ArrayList;

/**
 * Created by Schamper on 10/8/2014.
 */
public class SpeedyQuickReturnOnScrollListener extends QuickReturnOnScrollListener {

    //region Member Variables
    protected Animation mSlideHeaderUpAnimation;
    protected Animation mSlideHeaderDownAnimation;
    protected Animation mSlideFooterUpAnimation;
    protected Animation mSlideFooterDownAnimation;
    private ArrayList<View> mHeaderViews;
    private ArrayList<View> mFooterViews;
    //endregion
    
    public SpeedyQuickReturnOnScrollListener(Context context, QuickReturnType quickReturnType, View headerView, View footerView) {
        super(quickReturnType, headerView, 0, footerView, 0);

        mSlideHeaderUpAnimation = AnimationUtils.loadAnimation(context, R.anim.anticipate_slide_header_up);
        mSlideHeaderDownAnimation = AnimationUtils.loadAnimation(context, R.anim.overshoot_slide_header_down);
        mSlideFooterUpAnimation = AnimationUtils.loadAnimation(context, R.anim.overshoot_slide_footer_up);
        mSlideFooterDownAnimation = AnimationUtils.loadAnimation(context, R.anim.anticipate_slide_footer_down);
    }

    public SpeedyQuickReturnOnScrollListener(QuickReturnType quickReturnType, ArrayList<View> headerViews, ArrayList<View> footerViews) {
        super(quickReturnType, null, 0, null, 0);
        mHeaderViews = headerViews;
        mFooterViews = footerViews;
    }

    @Override
    protected void handleOnScrollChanged(int diff, int scrollY) {
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
    }

    @Override
    public boolean isHeaderHidden() {
        return mHeader.getVisibility() == View.GONE;
    }

    @Override
    public boolean isFooterHidden() {
        return mFooter.getVisibility() == View.GONE;
    }

    @Override
    public void showHeader() {
        toggleSpeedyAnimation(1, mHeader, mSlideHeaderDownAnimation, mSlideHeaderUpAnimation);
    }

    @Override
    public void hideHeader() {
        toggleSpeedyAnimation(-1, mHeader, mSlideHeaderDownAnimation, mSlideHeaderUpAnimation);
    }

    @Override
    public void showFooter() {
        toggleSpeedyAnimation(1, mFooter, mSlideFooterUpAnimation, mSlideFooterDownAnimation);
    }

    @Override
    public void hideFooter() {
        toggleSpeedyAnimation(-1, mFooter, mSlideFooterUpAnimation, mSlideFooterDownAnimation);
    }

    @Override
    public void setCanSlideInIdleScrollState(boolean canSlideInIdleScrollState) {
        throw new UnsupportedOperationException(getClass().getSimpleName() + " does not support/need this.");
    }

    public void toggleSpeedyAnimation(int diff, View view, Animation appearAnim, Animation hideAnim) {
        if (diff < 0) {
            if (view.getVisibility() == View.VISIBLE) {
                view.setVisibility(View.GONE);
                view.startAnimation(hideAnim);
            }
        } else if (diff > 0) {
            if (view.getVisibility() == View.GONE) {
                view.setVisibility(View.VISIBLE);
                view.startAnimation(appearAnim);
            }
        }
    }

    public void setSlideHeaderUpAnimation(Animation animation){
        mSlideHeaderUpAnimation = animation;
    }

    public void setSlideHeaderDownAnimation(Animation animation){
        mSlideHeaderDownAnimation = animation;
    }

    public void setSlideFooterUpAnimation(Animation animation){
        mSlideFooterUpAnimation = animation;
    }

    public void setSlideFooterDownAnimation(Animation animation){
        mSlideFooterDownAnimation = animation;
    }
}
