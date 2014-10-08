package com.etiennelawlor.quickreturn.library.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.AbsListView;
import android.widget.ScrollView;

/**
 * @author Cyril Mottier
 */
public class NotifyingScrollView extends ScrollView {

    // region Member Variables
    private boolean mIsOverScrollEnabled = true;
    private boolean isScrolling;
    private boolean isTouching;
    protected Runnable scrollingRunnable;
    private OnScrollChangedListener mOnScrollChangedListener;
    // endregion

    // region Interfaces
    public interface OnScrollChangedListener {
        void onScrollStateChanged(ScrollView who, int scrollState);
        void onScrollChanged(ScrollView who, int l, int t, int oldl, int oldt);
    }
    // endregion

    // region Constructors
    public NotifyingScrollView(Context context) {
        super(context);
    }

    public NotifyingScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public NotifyingScrollView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }
    // endregion

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);

        if (Math.abs(oldt - t) > 0) {
            if (scrollingRunnable != null) {
                removeCallbacks(scrollingRunnable);
            }
            scrollingRunnable = new Runnable() {
                public void run () {
                    if (isScrolling && !isTouching) {
                        if (mOnScrollChangedListener != null) {
                            mOnScrollChangedListener.onScrollStateChanged(NotifyingScrollView.this, AbsListView.OnScrollListener.SCROLL_STATE_IDLE);
                        }
                    }
                    isScrolling = false;
                    scrollingRunnable = null;
                }
            };
            postDelayed(scrollingRunnable, 200);
        }

        if (mOnScrollChangedListener != null) {
            mOnScrollChangedListener.onScrollChanged(this, l, t, oldl, oldt);
        }
    }

    @Override
    public boolean onTouchEvent ( MotionEvent ev ) {
        int action = ev.getAction();
        if (action == MotionEvent.ACTION_MOVE) {
            isTouching = true;
            isScrolling = true;
        } else if (action == MotionEvent.ACTION_UP) {
            if (isTouching && !isScrolling) {
                if (mOnScrollChangedListener != null) {
                    mOnScrollChangedListener.onScrollStateChanged(this, AbsListView.OnScrollListener.SCROLL_STATE_IDLE);
                }
            }
            isTouching = false;
        }
        return super.onTouchEvent(ev);
    }

    @Override
    protected boolean overScrollBy(int deltaX, int deltaY, int scrollX, int scrollY, int scrollRangeX, int scrollRangeY,
                                   int maxOverScrollX, int maxOverScrollY, boolean isTouchEvent) {
        return super.overScrollBy(
                deltaX,
                deltaY,
                scrollX,
                scrollY,
                scrollRangeX,
                scrollRangeY,
                mIsOverScrollEnabled ? maxOverScrollX : 0,
                mIsOverScrollEnabled ? maxOverScrollY : 0,
                isTouchEvent);
    }

    // region Helper Methods
    public void setOnScrollListener(OnScrollChangedListener listener) {
        mOnScrollChangedListener = listener;
    }

    public void setOverScrollEnabled(boolean enabled) {
        mIsOverScrollEnabled = enabled;
    }

    public boolean isOverScrollEnabled() {
        return mIsOverScrollEnabled;
    }
    // endregion

}