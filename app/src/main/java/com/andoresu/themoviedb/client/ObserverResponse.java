package com.andoresu.themoviedb.client;


import android.annotation.SuppressLint;
import android.support.annotation.NonNull;
import android.util.Log;

import com.andoresu.themoviedb.utils.BaseView;
import com.google.gson.JsonSyntaxException;

import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import retrofit2.Response;

@SuppressLint("LogNotTimber")
public class ObserverResponse<BaseResponse> implements Observer<BaseResponse> {

    private static final String TAG = "THEMOVIEDB_" + ObserverResponse.class.getSimpleName();

    private final BaseView view;

    private final boolean showGlobalError;

    public ErrorResponse errorResponse;

    public ObserverResponse() {
        this.view = null;
        this.showGlobalError = true;
        // is used to specify when the app is idle.
    }

    public ObserverResponse(boolean showGlobalError) {
        this.view = null;
        this.showGlobalError = showGlobalError;
        // is used to specify when the app is idle.
    }

    public ObserverResponse(@NonNull BaseView baseView) {
        this.view = baseView;
        this.view.showProgressIndicator(true);
        this.showGlobalError = true;
        // is used to specify when the app is idle.
    }

    public ObserverResponse(@NonNull BaseView baseView, boolean showGlobalError) {
        this.view = baseView;
        this.view.showProgressIndicator(true);
        this.showGlobalError = showGlobalError;
        // is used to specify when the app is idle.
    }

    @Override
    public void onSubscribe(Disposable d) {

    }


    @Override
    public void onNext(BaseResponse baseResponse) {
        try{
            Response response = (Response) baseResponse;
            if (!response.isSuccessful()){
                errorResponse = ErrorResponse.response(response);
                if(view != null && showGlobalError){
                    view.showGlobalError(errorResponse);
                }
                Log.e(TAG, "ObserverResponse Error: " + errorResponse.toString() );
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }


    @Override
    public void onError(Throwable e) {
        e.printStackTrace();
        if(view != null && showGlobalError){
            if(e instanceof JsonSyntaxException){
                view.showGlobalError(ErrorResponse.getBadJsonResponse());
            }else if(e instanceof UnknownHostException){
                view.showGlobalError(ErrorResponse.failConnection());
            }else if(e instanceof SocketTimeoutException) {
                view.showGlobalError(ErrorResponse.timeOut());
            }else if(e instanceof RuntimeException) {
                view.showGlobalError(ErrorResponse.runTimeException());
            }else if(e instanceof ConnectException){
                view.showGlobalError(ErrorResponse.failConnection());
            }else{
                view.showGlobalError(ErrorResponse.getFailErrorResponse());
                Log.e(TAG, "onError: ", e);
                Log.e(TAG, "onError profileType: " + e.getClass().getSimpleName());
                Log.e(TAG, "onError message: " + e.getMessage());
            }
        }
        onComplete();
    }

    @Override
    public void onComplete() {
        if( view != null ){
            view.showProgressIndicator(false);
        }
    }
}

