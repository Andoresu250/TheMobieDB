package com.andoresu.themoviedb.core.movies;

import com.andoresu.themoviedb.core.authorization.data.User;
import com.andoresu.themoviedb.core.movies.data.Movie;
import com.andoresu.themoviedb.core.movies.data.Movies;
import com.andoresu.themoviedb.utils.BaseView;

import java.util.Map;


public interface MoviesContract {

    interface View extends BaseView {

        void showMovies(Movies movies);

    }

    interface  ActionsListener {

        void getMovies(Map<String, String> options);

        void getFavoritesMovies(Map<String, String> options, User user);

    }

    interface InteractionListener {

        void movieDetail(Movie movie);

    }

}
