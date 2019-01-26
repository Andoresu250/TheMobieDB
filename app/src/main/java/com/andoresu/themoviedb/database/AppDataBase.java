package com.andoresu.themoviedb.database;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.TypeConverters;

import com.andoresu.themoviedb.core.authorization.data.User;
import com.andoresu.themoviedb.core.movies.data.Movie;

@Database(entities = {Movie.class, User.class}, version = 1)
@TypeConverters({Converters.class})
public abstract class AppDataBase extends RoomDatabase {

    public abstract DataBaseDao dataBaseDao();

}
