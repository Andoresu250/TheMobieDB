package com.andoresu.themoviedb.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.util.Log;

import com.andoresu.themoviedb.core.movies.data.Movie;
import com.andoresu.themoviedb.database.AppDataBase;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import static com.andoresu.themoviedb.utils.MyUtils.saveImage;

public class GlideListener implements RequestListener<Drawable> {

    public static final int POSTER_TYPE = 1;
    public static final int BACKDROP_TYPE= 2;

    private final AppDataBase appDataBase;
    private final int type;
    private final Movie movie;
    private final Context context;

    public GlideListener(AppDataBase appDataBase, int type, Movie movie, Context context){
        this.appDataBase = appDataBase;
        this.type = type;
        this.movie = movie;
        this.context = context;
    }

    @Override
    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
        return false;
    }

    @Override
    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
        BitmapDrawable drawable = (BitmapDrawable) resource;
        Bitmap bitmap = drawable.getBitmap();
        switch (type){
            case POSTER_TYPE:
                if(movie.poster == null){
                    appDataBase.dataBaseDao().updateMoviePoster(movie.id, saveImage(context, bitmap, movie.posterPath));
                }
                break;
            case BACKDROP_TYPE:
                if(movie.backdrop == null){
                    appDataBase.dataBaseDao().updateMovieBackdrop(movie.id, saveImage(context, bitmap, movie.backdropPath));
                }
                break;
        }

        return false;
    }
}
