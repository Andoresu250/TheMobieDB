package com.andoresu.themoviedb.core;

import android.content.Context;
import android.support.annotation.NonNull;

import com.andoresu.themoviedb.client.ObserverResponse;
import com.andoresu.themoviedb.client.ServiceGenerator;
import com.andoresu.themoviedb.core.authorization.SessionService;
import com.andoresu.themoviedb.core.authorization.data.Session;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;
import retrofit2.Response;

public class MainPresenter implements MainContract.ActionsListener {

    private final MainContract.View mainView;

    private final Context context;

    private final SessionService sessionService;

    public MainPresenter(@NonNull MainContract.View mainView, @NonNull Context context){
        this.mainView = mainView;
        this.context = context;
        this.sessionService = ServiceGenerator.createAPIService(SessionService.class);
    }


    @Override
    public void logout(Session session) {
        sessionService.logout(session.sessionId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new ObserverResponse<Response<ResponseBody>>(){});
    }
}
