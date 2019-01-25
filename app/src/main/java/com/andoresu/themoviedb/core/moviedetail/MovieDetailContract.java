package com.andoresu.themoviedb.core.moviedetail;

import com.andoresu.themoviedb.core.authorization.data.User;
import com.andoresu.themoviedb.core.movies.data.Movie;
import com.andoresu.themoviedb.core.movies.data.Movies;
import com.andoresu.themoviedb.utils.BaseView;

import java.util.Map;

public interface MovieDetailContract {
    interface View extends BaseView {

        void showMovie(Movie movie);

    }

    interface  ActionsListener {

        void getMovie(Integer movieId);

        void addMovieToFavorites(User user, Movie movie);

    }

    interface InteractionListener {

    }
}
