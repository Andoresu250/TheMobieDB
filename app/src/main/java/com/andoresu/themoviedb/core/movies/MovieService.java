package com.andoresu.themoviedb.core.movies;

import com.andoresu.themoviedb.core.movies.data.FavoriteRequest;
import com.andoresu.themoviedb.core.movies.data.Genre;
import com.andoresu.themoviedb.core.movies.data.Movie;
import com.andoresu.themoviedb.core.movies.data.Movies;
import com.andoresu.themoviedb.core.movies.data.SimpleBody;

import java.util.List;
import java.util.Map;

import io.reactivex.Observable;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;

public interface MovieService {

    @GET("discover/movie")
    Observable<Response<Movies>> discover(@QueryMap Map<String, String> options);

    @GET("account/{account_id}/favorite/movies")
    Observable<Response<Movies>> favorites(@Path("account_id") Integer accountId, @QueryMap Map<String, String> options, @Query("session_id") String sessionId);

    @GET("movie/now_playing")
    Observable<Response<Movies>> nowPlaying(@QueryMap Map<String, String> options);

    @GET("genre/movie/list")
    Observable<Response<Map<String, List<Genre>>>> genres();

    @GET("movie/{id}?&append_to_response=videos,casts")
    Observable<Response<Movie>> get(@Path("id") Integer id);

    @POST("account/{account_id}/favorite")
    Observable<Response<SimpleBody>> addToFavorites(@Path("account_id") Integer accountId, @Body FavoriteRequest favoriteRequest, @Query("session_id") String sessionId);
}
