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
    private int mPrevScrollY = 0;

    protected abstract void handleOnScrollChanged(int oldY, int newY);
    protected abstract void handleOnScrollStateChanged(int scrollState);

    @Override
    public void onScrollStateChanged(ScrollView who, int scrollState) {
        fireExtraOnScrollStateChangedListeners(who, scrollState);
        handleOnScrollStateChanged(scrollState);
    }

    @Override
    public void onScrollChanged(ScrollView who, int l, int t, int oldl, int oldt) {
        fireExtraOnScrollListeners(who, l, t, oldl, oldt);
        handleOnScrollChanged(oldt, t);
    }

    @Override
    public void onScrollStateChanged(AbsListView listView, int scrollState) {
        fireExtraOnScrollStateChangedListeners(listView, scrollState);
        handleOnScrollStateChanged(scrollState);
    }

    @Override
    public void onScroll(AbsListView listView, int i, int i2, int i3) {
        fireExtraOnScrollListeners(listView, i, i2, i3);
        int scrollY = QuickReturnUtils.getScrollY(listView);
        handleOnScrollChanged(mPrevScrollY, scrollY);
        mPrevScrollY = scrollY;
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
