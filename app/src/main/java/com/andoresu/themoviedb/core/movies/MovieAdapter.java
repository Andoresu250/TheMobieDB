package com.andoresu.themoviedb.core.movies;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.andoresu.themoviedb.R;
import com.andoresu.themoviedb.core.movies.data.Movie;
import com.andoresu.themoviedb.core.movies.data.Movies;
import com.andoresu.themoviedb.utils.BaseRecyclerViewAdapter;
import com.bumptech.glide.Glide;
import com.github.lzyzsd.circleprogress.ArcProgress;

import java.util.List;

import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;

import static com.andoresu.themoviedb.utils.MyUtils.glideRequestOptions;

public class MovieAdapter extends BaseRecyclerViewAdapter<Movie> {

    public Movies movies;

    public MovieAdapter(Context context, @NonNull OnItemClickListener<Movie> listener) {
        super(context, listener);
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
                .load(movie.getPosterThumbPath())
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
