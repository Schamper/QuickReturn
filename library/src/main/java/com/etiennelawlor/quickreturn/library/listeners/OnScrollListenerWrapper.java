package com.etiennelawlor.quickreturn.library.listeners;

import android.widget.AbsListView;
import android.widget.ScrollView;

import com.etiennelawlor.quickreturn.library.utils.QuickReturnUtils;
import com.etiennelawlor.quickreturn.library.views.NotifyingScrollView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Schamper on 10/8/2014.
 */
public abstract class OnScrollListenerWrapper implements AbsListView.OnScrollListener, NotifyingScrollView.OnScrollChangedListener {

    private List<AbsListView.OnScrollListener> mExtraListViewOnScrollListenerList =
            new ArrayList<AbsListView.OnScrollListener>();
    private List<NotifyingScrollView.OnScrollChangedListener> mExtraScrollViewOnScrollListenerList =
            new ArrayList<NotifyingScrollView.OnScrollChangedListener>();
    private int mLastScrollState = 0;
    private int mScrollY = 0;

    private QuickReturnUtils mUtils;

    protected abstract void handleOnScrollChanged(int diff, int scrollY);
    protected abstract void handleOnScrollStateChanged(int scrollState, int scrollY);

    @Override
    public void onScrollStateChanged(ScrollView who, int scrollState) {
        fireExtraOnScrollStateChangedListeners(who, scrollState);
        handleOnScrollStateChanged(scrollState, who.getScrollY());
    }

    @Override
    public void onScrollChanged(ScrollView who, int l, int t, int oldl, int oldt) {
        fireExtraOnScrollListeners(who, l, t, oldl, oldt);
        handleOnScrollChanged(oldt - t, t);
    }

    @Override
    public void onScrollStateChanged(AbsListView listView, int scrollState) {
        fireExtraOnScrollStateChangedListeners(listView, scrollState);

        handleOnScrollStateChanged(scrollState, mScrollY);
        mLastScrollState = scrollState;
    }

    @Override
    public void onScroll(AbsListView listView, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        fireExtraOnScrollListeners(listView, firstVisibleItem, visibleItemCount, totalItemCount);

        if (mUtils == null) {
            mUtils = new QuickReturnUtils(listView);
        }

        int diff = mUtils.getIncrementalOffset(firstVisibleItem, visibleItemCount);
        mScrollY -= diff;

        if (mLastScrollState != SCROLL_STATE_IDLE) {
            handleOnScrollChanged(diff, mScrollY);
        }
    }

    public void registerExtraOnScrollListener(AbsListView.OnScrollListener listener) {
        mExtraListViewOnScrollListenerList.add(listener);
    }

    public void fireExtraOnScrollStateChangedListeners(AbsListView view, int scrollState) {
        for (AbsListView.OnScrollListener listener : mExtraListViewOnScrollListenerList) {
            listener.onScrollStateChanged(view, scrollState);
        }
    }

    public void fireExtraOnScrollListeners(AbsListView listView, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        for (AbsListView.OnScrollListener listener : mExtraListViewOnScrollListenerList) {
            listener.onScroll(listView, firstVisibleItem, visibleItemCount, totalItemCount);
        }
    }

    public void registerExtraOnScrollListener(NotifyingScrollView.OnScrollChangedListener listener) {
        mExtraScrollViewOnScrollListenerList.add(listener);
    }

    public void fireExtraOnScrollStateChangedListeners(ScrollView view, int scrollState) {
        for (NotifyingScrollView.OnScrollChangedListener listener : mExtraScrollViewOnScrollListenerList) {
            listener.onScrollStateChanged(view, scrollState);
        }
    }

    public void fireExtraOnScrollListeners(ScrollView who, int l, int t, int oldl, int oldt) {
        for (NotifyingScrollView.OnScrollChangedListener listener : mExtraScrollViewOnScrollListenerList) {
            listener.onScrollChanged(who, l, t, oldl, oldt);
        }
    }
}
