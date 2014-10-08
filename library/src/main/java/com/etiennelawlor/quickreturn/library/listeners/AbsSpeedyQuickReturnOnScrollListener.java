package com.etiennelawlor.quickreturn.library.listeners;

import android.content.Context;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.etiennelawlor.quickreturn.library.R;
import com.etiennelawlor.quickreturn.library.enums.QuickReturnType;

/**
 * Created by Schamper on 10/8/2014.
 */
public abstract class AbsSpeedyQuickReturnOnScrollListener extends AbsBaseQuickReturnOnScrollListener {

    //region Member Variables
    protected Context mContext;
    protected View mHeader;
    protected View mFooter;
    protected QuickReturnType mQuickReturnType;
    protected Animation mSlideHeaderUpAnimation;
    protected Animation mSlideHeaderDownAnimation;
    protected Animation mSlideFooterUpAnimation;
    protected Animation mSlideFooterDownAnimation;
    //endregion
    
    public AbsSpeedyQuickReturnOnScrollListener(Context context, QuickReturnType quickReturnType, View headerView, View footerView) {
        mContext = context;
        mQuickReturnType = quickReturnType;

        mSlideHeaderUpAnimation = AnimationUtils.loadAnimation(mContext, R.anim.anticipate_slide_header_up);
        mSlideHeaderDownAnimation = AnimationUtils.loadAnimation(mContext, R.anim.overshoot_slide_header_down);
        mSlideFooterUpAnimation = AnimationUtils.loadAnimation(mContext, R.anim.overshoot_slide_footer_up);
        mSlideFooterDownAnimation = AnimationUtils.loadAnimation(mContext, R.anim.anticipate_slide_footer_down);

        mHeader =  headerView;
        mFooter =  footerView;
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
