package com.etiennelawlor.quickreturn.library.listeners;

import android.widget.AbsListView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Schamper on 10/8/2014.
 */
public abstract class AbsBaseQuickReturnOnScrollListener {

    private List<AbsListView.OnScrollListener> mExtraOnScrollListenerList = new ArrayList<AbsListView.OnScrollListener>();

    public void registerExtraOnScrollListener(AbsListView.OnScrollListener listener) {
        mExtraOnScrollListenerList.add(listener);
    }

    public void fireExtraOnScrollStateChangedListeners(AbsListView view, int scrollState) {
        for (AbsListView.OnScrollListener listener : mExtraOnScrollListenerList) {
            listener.onScrollStateChanged(view, scrollState);
        }
    }

    public void fireExtraOnScrollListeners(AbsListView listView, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        for (AbsListView.OnScrollListener listener : mExtraOnScrollListenerList) {
            listener.onScroll(listView, firstVisibleItem, visibleItemCount, totalItemCount);
        }
    }
}
