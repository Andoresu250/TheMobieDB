package com.andoresu.themoviedb.core.moviedetail;

import android.content.Context;

import com.andoresu.themoviedb.client.ObserverResponse;
import com.andoresu.themoviedb.client.ServiceGenerator;
import com.andoresu.themoviedb.core.authorization.data.User;
import com.andoresu.themoviedb.core.movies.MovieService;
import com.andoresu.themoviedb.core.movies.MoviesContract;
import com.andoresu.themoviedb.core.movies.data.FavoriteRequest;
import com.andoresu.themoviedb.core.movies.data.Movie;
import com.andoresu.themoviedb.core.movies.data.SimpleBody;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Response;

public class MovieDetailPresenter implements MovieDetailContract.ActionsListener{

    private final MovieDetailContract.View view;

    private final Context context;

    private final MovieService movieService;

    public MovieDetailPresenter(MovieDetailContract.View view, Context context){
        this.context = context;
        this.view = view;
        this.movieService = ServiceGenerator.createAPIService(MovieService.class);
    }

    @Override
    public void getMovie(Integer movieId) {
        movieService.get(movieId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new ObserverResponse<Response<Movie>>(){
                    @Override
                    public void onNext(Response<Movie> movieResponse) {
                        super.onNext(movieResponse);
                        if(movieResponse.isSuccessful()){
                            Movie movie = movieResponse.body();
                            if(movie != null){
                                view.showMovie(movie);
                            }
                        }
                    }
                });

    }

    @Override
    public void addMovieToFavorites(User user, Movie movie) {
        movieService.addToFavorites(user.id, new FavoriteRequest(movie), user.sessionId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new ObserverResponse<Response<SimpleBody>>(){
                    @Override
                    public void onNext(Response<SimpleBody> simpleBodyResponse) {
                        super.onNext(simpleBodyResponse);
                        if(simpleBodyResponse.isSuccessful()){
                            SimpleBody simpleBody = simpleBodyResponse.body();
                            if(simpleBody != null){
                                view.showMessage(simpleBody.statusMessage);
                            }
                        }
                    }
                });
    }
}
