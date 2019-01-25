package com.andoresu.themoviedb.client;

import android.annotation.SuppressLint;
import android.support.annotation.NonNull;
import android.util.Log;

import com.andoresu.themoviedb.utils.BaseView;
import com.google.gson.JsonSyntaxException;

import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

import io.reactivex.SingleObserver;
import io.reactivex.disposables.Disposable;
import retrofit2.Response;

@SuppressLint("LogNotTimber")
public class SingleResponse<BaseResponse> implements SingleObserver<BaseResponse> {

    private static final String TAG = "THEMOVIEDB_" + SingleResponse.class.getSimpleName();

    private final BaseView view;

    private final boolean showGlobalError;

    public SingleResponse() {
        this.view = null;
        this.showGlobalError = true;
    }

    public SingleResponse(boolean showGlobalError) {
        this.view = null;
        this.showGlobalError = showGlobalError;
    }

    public SingleResponse(@NonNull BaseView baseView) {
        this.view = baseView;
        this.view.showProgressIndicator(true);
        this.showGlobalError = true;
    }

    public SingleResponse(@NonNull BaseView baseView, boolean showGlobalError) {
        this.view = baseView;
        this.view.showProgressIndicator(true);
        this.showGlobalError = showGlobalError;
    }

    @Override
    public void onSubscribe(Disposable d) {

    }

    @Override
    public void onSuccess(BaseResponse baseResponse) {
        try{
            Response response = (Response) baseResponse;
            if (!response.isSuccessful()){
                ErrorResponse errorResponse = ErrorResponse.response(response);
                if(view != null && showGlobalError){
                    view.showGlobalError(errorResponse);
                }
                Log.e(TAG, "onSuccess: " + errorResponse );
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        onComplete(baseResponse);
    }

    @Override
    public void onError(Throwable e) {
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
        onComplete(null);
    }

    public void onComplete(BaseResponse baseResponse) {
        if( view != null ){
            view.showProgressIndicator(false);
        }
    }
}
