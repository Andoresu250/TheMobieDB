package com.andoresu.themoviedb.database;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.andoresu.themoviedb.core.authorization.data.User;
import com.andoresu.themoviedb.core.movies.data.Movie;

import java.util.List;

@Dao
public interface DataBaseDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public void addMovie(Movie movie);

    @Query("select * from movie where id = :id limit 1")
    public Movie getMovie(Integer id);

    @Update
    public void updateMovie(Movie movie);

    @Query("UPDATE movie SET poster = :poster WHERE id = :id")
    public void updateMoviePoster(Integer id, String poster);

    @Query("UPDATE movie SET favorite = 'true' WHERE id = :id")
    public void addMovieToFavorite(Integer id);

    @Query("UPDATE movie SET backdrop = :backdrop WHERE id = :id")
    public void updateMovieBackdrop(Integer id, String backdrop);

    @Query("select * from movie order by popularity desc")
    public List<Movie> getMovies();

    @Query("select * from movie  where favorite = 'true' order by popularity desc")
    public List<Movie> getFavorites();

    @Query("select * from user limit 1")
    public User getLoggedUser();

    @Query("DELETE from user")
    public void deleteUser();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public void addUser(User user);



}
