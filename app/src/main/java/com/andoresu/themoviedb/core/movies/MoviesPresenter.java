package com.andoresu.themoviedb.core.movies;

import android.arch.persistence.room.Room;
import android.content.Context;
import android.util.Log;

import com.andoresu.themoviedb.client.ObserverResponse;
import com.andoresu.themoviedb.client.ServiceGenerator;
import com.andoresu.themoviedb.core.authorization.data.User;
import com.andoresu.themoviedb.core.movies.data.Genre;
import com.andoresu.themoviedb.core.movies.data.Movie;
import com.andoresu.themoviedb.core.movies.data.Movies;
import com.andoresu.themoviedb.database.AppDataBase;

import java.util.List;
import java.util.Map;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Response;

public class MoviesPresenter implements MoviesContract.ActionsListener{

    public static final String TAG = "THEMOVIEDB_" + MoviesPresenter.class.getSimpleName();

    private final MoviesContract.View view;

    private final Context context;

    private final MovieService movieService;

    private List<Genre> genres;

    private final AppDataBase appDataBase;

    public MoviesPresenter(MoviesContract.View view, Context context) {
        this.view = view;
        this.context = context;
        this.movieService = ServiceGenerator.createAPIService(MovieService.class);
        this.appDataBase = Room.databaseBuilder(context, AppDataBase.class, "moviesdb").allowMainThreadQueries().build();
    }

    @Override
    public void getMovies(Map<String, String> options) {
        if(genres == null){
            getMoviesWithGenres(options, false, null);
        }else{
            getMovies(options, genres);
        }
    }

    @Override
    public void getFavoritesMovies(Map<String, String> options, User user) {
        if(genres == null){
            getMoviesWithGenres(options, true, user);
        }else{
            getFavorites(options, genres, user);
        }
    }

    public void getMoviesWithGenres(Map<String, String> options, boolean favorites, User user){
        view.showProgressIndicator(true);
        movieService.genres()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new ObserverResponse<Response<Map<String, List<Genre>>>>(){
                    @Override
                    public void onNext(Response<Map<String, List<Genre>>> mapResponse) {
                        super.onNext(mapResponse);
                        if(mapResponse.isSuccessful()){
                            Map<String, List<Genre>> genreHash = mapResponse.body();
                            if(genreHash != null){
                                List<Genre> genres = genreHash.get("genres");
                                MoviesPresenter.this.genres = genres;
                                if(favorites){
                                    getFavorites(options, genres, user);
                                }else{
                                    getMovies(options, genres);
                                }
                                return;
                            }
                        }
                        showLocalMovies(favorites);
                    }
                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        showLocalMovies(favorites);
                    }
                });

    }

    public void getMovies(Map<String, String> options, List<Genre> genres){
        movieService.discover(options)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(moviesObserver(genres, false));
    }

    public void getFavorites(Map<String, String> options, List<Genre> genres, User user){
        movieService.favorites(user.id, options, user.sessionId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(moviesObserver(genres, true));
    }

    public ObserverResponse<Response<Movies>> moviesObserver(List<Genre> genres, boolean favorites){
        return new ObserverResponse<Response<Movies>>(view){
            @Override
            public void onNext(Response<Movies> moviesResponse) {
                super.onNext(moviesResponse);
                if(moviesResponse.isSuccessful()){
                    Movies movies = moviesResponse.body();
                    if(movies != null){
                        for(Movie movie : movies.results){
                            movie.setGenres(genres);
                            movie.favorite = favorites;
                        }
                        saveMovies(movies.results);
                        view.showMovies(movies);
                        return;
                    }
                }
                showLocalMovies(favorites);
            }

            @Override
            public void onError(Throwable e) {
                super.onError(e);
                showLocalMovies(favorites);
            }
        };
    }

    public void saveMovies(List<Movie> movies){
        for(Movie movie : movies){
            movie.init();
            appDataBase.dataBaseDao().addMovie(movie);
        }
    }

    public List<Movie> getLocalMovies(){
        return appDataBase.dataBaseDao().getMovies();
    }

    public List<Movie> getLocalFavorites(){
        return appDataBase.dataBaseDao().getFavorites();
    }

    public void showLocalMovies(boolean favorites){
        List<Movie> movies = favorites ? getLocalFavorites() : getLocalMovies();
        if(movies != null){
            view.showMovies(new Movies(movies));
        }else{
            view.showMovies(null);
        }
        view.showProgressIndicator(false);
    }


}
