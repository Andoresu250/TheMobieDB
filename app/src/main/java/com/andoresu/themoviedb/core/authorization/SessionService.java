package com.andoresu.themoviedb.core.authorization;

import com.andoresu.themoviedb.core.authorization.data.LoginRequest;
import com.andoresu.themoviedb.core.authorization.data.RequestToken;
import com.andoresu.themoviedb.core.authorization.data.Session;
import com.andoresu.themoviedb.core.authorization.data.User;

import io.reactivex.Observable;
import io.reactivex.Single;
import okhttp3.ResponseBody;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.HTTP;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface SessionService {

    @GET("authentication/token/new")
    Observable<Response<RequestToken>> getRequestToken();

    @POST("authentication/token/validate_with_login")
    Observable<Response<RequestToken>> getLoginRequestToken(@Body LoginRequest loginRequest);

    @POST("authentication/session/new")
    Observable<Response<Session>> createSession(@Body LoginRequest loginRequest);

    @GET("account")
    Observable<Response<User>> getProfile(@Query("session_id") String sessionId);

    @FormUrlEncoded
    @HTTP(method = "DELETE", path = "authentication/session", hasBody = true)
    Observable<Response<ResponseBody>> logout(@Field("session_id") String sessionId);

}
