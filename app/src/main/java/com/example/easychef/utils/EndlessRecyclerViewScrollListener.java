package com.example.easychef.utils;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public abstract class EndlessRecyclerViewScrollListener extends RecyclerView.OnScrollListener {

    private static final int VISIBLE_THRESHOLD = 2;
    private int currentOffsetIndex = 0;
    private int previousTotalItemCount = 0;
    private boolean isDataLoading = true;
    private final RecyclerView.LayoutManager endlessScrollManager;

    public EndlessRecyclerViewScrollListener(LinearLayoutManager layoutManager) {
        this.endlessScrollManager = layoutManager;
    }

    public abstract void onLoadMore(int page, int totalItemsCount, RecyclerView view);

    @Override
    public void onScrolled(@NonNull RecyclerView view, int dx, int dy) {
        final int lastVisibleItemPosition = ((LinearLayoutManager) endlessScrollManager).findLastVisibleItemPosition();
        final int totalItemCount = endlessScrollManager.getItemCount();

        if (totalItemCount < previousTotalItemCount) {
            currentOffsetIndex = 0;
            previousTotalItemCount = totalItemCount;
            if (totalItemCount == 0) {
                isDataLoading = true;
            }
        }

        if (isDataLoading && (totalItemCount > previousTotalItemCount)) {
            isDataLoading = false;
            previousTotalItemCount = totalItemCount;
        }

        if (!isDataLoading && (lastVisibleItemPosition + VISIBLE_THRESHOLD) >= totalItemCount) {
            currentOffsetIndex++;
            onLoadMore(currentOffsetIndex, totalItemCount, view);
            isDataLoading = true;
        }
    }
}