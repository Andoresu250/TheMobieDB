package com.andoresu.themoviedb.core.movies;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.andoresu.themoviedb.R;
import com.andoresu.themoviedb.core.movies.data.Movie;
import com.andoresu.themoviedb.core.movies.data.Movies;
import com.andoresu.themoviedb.database.AppDataBase;
import com.andoresu.themoviedb.utils.BaseRecyclerViewAdapter;
import com.andoresu.themoviedb.utils.GlideListener;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.github.lzyzsd.circleprogress.ArcProgress;

import java.io.File;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.andoresu.themoviedb.utils.MyUtils.glideRequestOptions;
import static com.andoresu.themoviedb.utils.MyUtils.saveImage;

public class MovieAdapter extends BaseRecyclerViewAdapter<Movie> {

    public static final String TAG = "THEMOVIEDB_" + MovieAdapter.class.getSimpleName();

    public Movies movies;
    private final AppDataBase appDataBase;

    public MovieAdapter(Context context, @NonNull OnItemClickListener<Movie> listener,@NonNull AppDataBase appDataBase) {
        super(context, listener);
        this.appDataBase = appDataBase;
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.item_movie;
    }

    @NonNull
    @Override
    public BaseViewHolder<Movie> onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(getLayoutResId(), parent, false);
        return new MovieViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BaseViewHolder<Movie> holder, int position) {
        super.onBindViewHolder(holder, position);
        MovieViewHolder viewHolder = (MovieViewHolder) holder;
        Movie movie = getItem(position);

        viewHolder.movieTextView.setText(getText(R.string.movie_title, movie.title, movie.getYear()));
        viewHolder.movieScoreArcProgress.setProgress(movie.getScore());
        viewHolder.movieGenreTextView.setText(movie.getGenres());
        Glide.with(context)
                .load(movie.poster == null ? movie.getPosterThumbPath() : Uri.fromFile(new File(movie.poster)))
                .listener(new GlideListener(appDataBase, GlideListener.POSTER_TYPE, movie, context))
                .apply(glideRequestOptions(context))
                .into(viewHolder.posterImageView);
    }

    public void setMovies(Movies movies){
        this.movies = movies;
        addAll(movies.results);
    }

    public static class MovieViewHolder extends BaseViewHolder<Movie> {

        @BindView(R.id.posterImageView)
        ImageView posterImageView;

        @BindView(R.id.movieTextView)
        TextView movieTextView;

        @BindView(R.id.movieScoreArcProgress)
        ArcProgress movieScoreArcProgress;

        @BindView(R.id.movieGenreTextView)
        TextView movieGenreTextView;

        public MovieViewHolder(View v){
            super(v);
            ButterKnife.bind(this, v);
        }

    }

}
