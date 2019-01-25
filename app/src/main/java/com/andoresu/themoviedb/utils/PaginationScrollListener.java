package com.andoresu.themoviedb.utils;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

public abstract class PaginationScrollListener extends RecyclerView.OnScrollListener{

    public static final String TAG = "THEMOVIEDB_" +  PaginationScrollListener.class.getSimpleName();

    private final LinearLayoutManager layoutManager;

    public PaginationScrollListener(LinearLayoutManager layoutManager) {
        this.layoutManager = layoutManager;
    }

    @Override
    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        super.onScrolled(recyclerView, dx, dy);

        int visibleItemCount = layoutManager.getChildCount();
        int totalItemCount = layoutManager.getItemCount();
        int firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition();

        if(dy > 0){
            if(!isLoading() && !isLastPage()){
                if ( (visibleItemCount + firstVisibleItemPosition) >= totalItemCount){
                    loadMoreItems();
                }
            }
        }

    }

    protected abstract void loadMoreItems();

    public abstract int getTotalPageCount();

    public abstract boolean isLastPage();

    public abstract boolean isLoading();

}
