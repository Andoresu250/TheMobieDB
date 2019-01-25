package com.andoresu.themoviedb.utils;


import com.andoresu.themoviedb.client.ErrorResponse;

public interface BaseView {

    void showProgressIndicator(boolean active);

    void showGlobalError(ErrorResponse errorResponse);

    void showMessage(String msg);

}
