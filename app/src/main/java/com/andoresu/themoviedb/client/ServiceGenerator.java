package com.andoresu.themoviedb.client;


import android.support.annotation.NonNull;

import com.google.gson.Gson;

import java.io.IOException;
import java.net.URL;
import java.util.concurrent.TimeUnit;

import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.andoresu.themoviedb.client.GsonBuilderUtils.SERIALIZED_GSON;


public class ServiceGenerator {

    public static final String TAG = "THEMOVIEDB_" + ServiceGenerator.class.getSimpleName();

    private static final String API_URL = "https://api.themoviedb.org/3/";
    public static final String API_IMAGE_URL = "https://image.tmdb.org/t/p/";


    public static <S> S createAPIService(Class<S> serviceClass){
        return createServiceUrl(serviceClass, API_URL);
    }

    private static <S> S createServiceUrl(Class<S> serviceClass, String url){
        AuthenticationInterceptor interceptor =
                new AuthenticationInterceptor();

        OkHttpClient.Builder httpClient =
                new OkHttpClient.Builder()
                        .addInterceptor(new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
                        .addInterceptor(interceptor)
                ;

        Retrofit.Builder builder =
                new Retrofit.Builder()
                        .baseUrl(url)
                        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                        .client(httpClient.build())
                        .addConverterFactory(GsonConverterFactory.create(SERIALIZED_GSON))
                ;

        Retrofit retrofit = builder.build();

        return retrofit.create(serviceClass);
    }

//    public static <S> S createService(Class<S> serviceClass) {
//        return createService(serviceClass, null, null);
//    }

    public static <S> S createService(Class<S> serviceClass, Gson gson) {

        AuthenticationInterceptor interceptor =
                new AuthenticationInterceptor();

        OkHttpClient.Builder httpClient =
                new OkHttpClient.Builder()
                        .addInterceptor(new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
                        .connectTimeout(20, TimeUnit.SECONDS)
                        .writeTimeout(20, TimeUnit.SECONDS)
                        .readTimeout(30, TimeUnit.SECONDS)
                ;

        if (!httpClient.interceptors().contains(interceptor)) {
            httpClient.addInterceptor(interceptor);
        }else{
            httpClient.interceptors().remove(interceptor);
            httpClient.addInterceptor(interceptor);
        }

        Retrofit.Builder builder =
                new Retrofit.Builder()
                        .baseUrl(API_URL)
                        .addCallAdapterFactory(RxJava2CallAdapterFactory.create());

        builder.client(httpClient.build());

        if(gson != null){
            builder.addConverterFactory(GsonConverterFactory.create(gson));
        }else{
            builder.addConverterFactory(GsonConverterFactory.create(SERIALIZED_GSON));
        }

        Retrofit retrofit = builder.build();

        return retrofit.create(serviceClass);
    }

    private static class AuthenticationInterceptor implements Interceptor {

        @Override
        public Response intercept(@NonNull Chain chain) throws IOException {
            Request original = chain.request();

            HttpUrl originalHttpUrl = original.url();
            HttpUrl url = originalHttpUrl.newBuilder()
                    .addQueryParameter("api_key", "0d677b16a44d2b5a79edf325bc1ee9b7")
                    .build();

            Request.Builder builder = original.newBuilder()
                    .header("Content-Type", "application/json")
                    .method(original.method(), original.body())
                    .url(url);

            Request request = builder.build();
            return chain.proceed(request);
        }
    }



}
