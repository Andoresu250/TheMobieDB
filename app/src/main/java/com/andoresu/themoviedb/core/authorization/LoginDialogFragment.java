package com.andoresu.themoviedb.core.authorization;

import android.app.Dialog;
import android.arch.persistence.room.Room;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.andoresu.themoviedb.R;
import com.andoresu.themoviedb.client.ObserverResponse;
import com.andoresu.themoviedb.client.ServiceGenerator;
import com.andoresu.themoviedb.core.authorization.data.LoginRequest;
import com.andoresu.themoviedb.core.authorization.data.RequestToken;
import com.andoresu.themoviedb.core.authorization.data.Session;
import com.andoresu.themoviedb.core.authorization.data.User;
import com.andoresu.themoviedb.database.AppDataBase;
import com.andoresu.themoviedb.utils.BaseDialogFragment;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Response;

import static com.andoresu.themoviedb.utils.MyUtils.goToWebsite;

public class LoginDialogFragment extends BaseDialogFragment {

    @BindView(R.id.userNameEditText)
    EditText usernameEditText;
    @BindView(R.id.passwordEditText)
    EditText passwordEditText;
    @BindView(R.id.userTextInputLayout)
    TextInputLayout usernameTextInputLayout;
    @BindView(R.id.passwordTextInputLayout)
    TextInputLayout passwordTextInputLayout;
    @BindView(R.id.loginBtn)
    Button loginButton;
    @BindView(R.id.signUpButton)
    Button signUpButton;
    @BindView(R.id.progressBar)
    ProgressBar progressBar;
    @BindView(R.id.loginLayout)
    View loginLayout;

    private SessionService sessionService;

    private CompositeDisposable disposable = new CompositeDisposable();

    private InteractionListener interactionListener;
    private AppDataBase appDataBase;

    public LoginDialogFragment(){}

    public static LoginDialogFragment newInstance(InteractionListener interactionListener) {

        Bundle args = new Bundle();
        LoginDialogFragment fragment = new LoginDialogFragment();
        fragment.setArguments(args);
        fragment.setInteractionListener(interactionListener);
        return fragment;
    }

    private void setInteractionListener(InteractionListener interactionListener) {
        this.interactionListener = interactionListener;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.sessionService = ServiceGenerator.createAPIService(SessionService.class);
        this.appDataBase =  Room.databaseBuilder(getContext(), AppDataBase.class, "moviesdb").allowMainThreadQueries().build();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        View view = getActivity().getLayoutInflater().inflate(R.layout.dialog_login, null);
        setView(view);
        setUnbinder(ButterKnife.bind(this, view));
        AlertDialog.Builder builder = getBuilder();

        loginButton.setOnClickListener(view1 -> login(usernameEditText.getText().toString(), passwordEditText.getText().toString()));
        signUpButton.setOnClickListener(view12 -> goToWebsite(getContext(), "https://www.themoviedb.org/account/signup"));

        builder.setView(view);

        return builder.create();
    }

    private void login(String username, String password){
        loginLayout.setVisibility(View.INVISIBLE);
        progressBar.setVisibility(View.VISIBLE);
        sessionService.getRequestToken()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new ObserverResponse<Response<RequestToken>>(){
                    @Override
                    public void onNext(Response<RequestToken> requestTokenResponse) {
                        super.onNext(requestTokenResponse);
                        if(requestTokenResponse.isSuccessful()){
                            RequestToken requestToken = requestTokenResponse.body();
                            if(requestToken != null){
                                LoginRequest loginRequest = new LoginRequest(username, password, requestToken.getToken());
                                sessionService.getLoginRequestToken(loginRequest)
                                        .subscribeOn(Schedulers.io())
                                        .observeOn(AndroidSchedulers.mainThread())
                                        .subscribe(new ObserverResponse<Response<RequestToken>>(){
                                            @Override
                                            public void onNext(Response<RequestToken> requestTokenResponse) {
                                                super.onNext(requestTokenResponse);
                                                if(requestTokenResponse.isSuccessful()){
                                                    RequestToken loginRequestToken = requestTokenResponse.body();
                                                    if(loginRequestToken != null){
                                                        loginRequest.requestToken = loginRequestToken.getToken();
                                                        sessionService.createSession(loginRequest)
                                                                .subscribeOn(Schedulers.io())
                                                                .observeOn(AndroidSchedulers.mainThread())
                                                                .subscribe(new ObserverResponse<Response<Session>>(){
                                                                    @Override
                                                                    public void onNext(Response<Session> sessionResponse) {
                                                                        super.onNext(sessionResponse);
                                                                        if(sessionResponse.isSuccessful()){
                                                                            Session session =  sessionResponse.body();
                                                                            if(session != null){
                                                                                sessionService.getProfile(session.sessionId)
                                                                                        .subscribeOn(Schedulers.io())
                                                                                        .observeOn(AndroidSchedulers.mainThread())
                                                                                        .subscribe(new ObserverResponse<Response<User>>(){
                                                                                            @Override
                                                                                            public void onNext(Response<User> userResponse) {
                                                                                                super.onNext(userResponse);
                                                                                                if(userResponse.isSuccessful()){
                                                                                                    User user = userResponse.body();
                                                                                                    if(user != null){
                                                                                                        user.requestToken = loginRequestToken.getToken();
                                                                                                        user.sessionId = session.sessionId;
                                                                                                        appDataBase.dataBaseDao().addUser(user);
                                                                                                        loginLayout.setVisibility(View.VISIBLE);
                                                                                                        progressBar.setVisibility(View.INVISIBLE);
                                                                                                        interactionListener.setUser(user);

                                                                                                        dismiss();
                                                                                                    }
                                                                                                }
                                                                                            }
                                                                                        });
                                                                            }
                                                                        }
                                                                    }
                                                                });
                                                    }
                                                }
                                            }
                                        });
                            }
                        }
                    }
                })
        ;
    }

    public interface InteractionListener{

        public void setUser(User user);

    }



}
