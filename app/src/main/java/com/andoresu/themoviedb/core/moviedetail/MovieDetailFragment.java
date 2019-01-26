package com.andoresu.themoviedb.core.moviedetail;



import android.arch.persistence.room.Room;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.andoresu.themoviedb.R;
import com.andoresu.themoviedb.core.movies.data.Movie;
import com.andoresu.themoviedb.database.AppDataBase;
import com.andoresu.themoviedb.utils.BaseFragment;
import com.andoresu.themoviedb.utils.GlideListener;
import com.bumptech.glide.Glide;
import com.github.ksoichiro.android.observablescrollview.ObservableScrollView;
import com.github.ksoichiro.android.observablescrollview.ObservableScrollViewCallbacks;
import com.github.ksoichiro.android.observablescrollview.ScrollState;
import com.github.lzyzsd.circleprogress.ArcProgress;

import java.io.File;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.andoresu.themoviedb.utils.MyUtils.glideRequestOptions;
import static com.andoresu.themoviedb.utils.MyUtils.watchYoutubeVideo;

public class MovieDetailFragment extends BaseFragment implements
        ObservableScrollViewCallbacks,
        MovieDetailContract.View {

    String TAG = "MOVIEDB_" + MovieDetailFragment.class.getSimpleName();

    @BindView(R.id.parallaxLayout)
    View parallaxLayout;

    @BindView(R.id.movieBackdropPathImageView)
    ImageView movieBackdropPathImageView;

    @BindView(R.id.alphaView)
    View alphaView;

    @BindView(R.id.observableScrollView)
    ObservableScrollView scrollView;

    @BindView(R.id.favoriteFloatingActionButton)
    FloatingActionButton favoriteFloatingActionButton;

    @BindView(R.id.posterImageView)
    ImageView posterImageView;

    @BindView(R.id.movieTextView)
    TextView movieTextView;

    @BindView(R.id.movieScoreArcProgress)
    ArcProgress movieScoreArcProgress;

    @BindView(R.id.movieGenreTextView)
    TextView movieGenreTextView;

    @BindView(R.id.youtubeButton)
    Button youtubeButton;

    @BindView(R.id.movieOverviewTextView)
    TextView movieOverviewTextView;

    @BindView(R.id.movieReleaseDateTextView)
    TextView movieReleaseDateTextView;

    @BindView(R.id.movieCastRecyclerView)
    RecyclerView movieCastRecyclerView;

    @BindView(R.id.movieCastTextView)
    TextView movieCastTextView;


    private int parallaxImageHeight;

    private Movie movie;

    private MovieDetailContract.ActionsListener actionsListener;

    private MovieDetailContract.InteractionListener interactionListener;

    private boolean favoriteVisible = true;

    private LinearLayoutManager linearLayoutManager;

    private ActorAdapter actorAdapter;
    private AppDataBase appDataBase;

    public MovieDetailFragment(){}

    public static MovieDetailFragment newInstance(MovieDetailContract.InteractionListener interactionListener, Movie movie) {
        Bundle args = new Bundle();
        args.putSerializable("movie", movie);
        MovieDetailFragment fragment = new MovieDetailFragment();
        fragment.setArguments(args);
        fragment.setInteractionListener(interactionListener);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        if(bundle != null){
            this.movie = (Movie) bundle.getSerializable("movie");
        }
        this.actionsListener = new MovieDetailPresenter(this, getContext());
        this.appDataBase = Room.databaseBuilder(getContext(), AppDataBase.class, "moviesdb").allowMainThreadQueries().build();

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_movie_detail, container, false);
        setUnbinder(ButterKnife.bind(this, view));
        scrollView.setScrollViewCallbacks(this);
        parallaxImageHeight = getResources().getDimensionPixelSize(R.dimen.parallax_image_height);
        youtubeButton.setOnClickListener(view1 -> watchYoutubeVideo(getContext(), movie.getYoutubeVideo().key));

        favoriteFloatingActionButton.setOnClickListener(view12 -> {
            if(user == null){
                mainView.loginRequired();
            }else{
                actionsListener.addMovieToFavorites(user, movie);
            }
        });

        showMovie(movie);
        linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        movieCastRecyclerView.setLayoutManager(linearLayoutManager);
        actorAdapter = new ActorAdapter(getContext());
        movieCastRecyclerView.setAdapter(actorAdapter);
        actionsListener.getMovie(movie.id);
        return view;
    }

    private void setInteractionListener(MovieDetailContract.InteractionListener interactionListener) {
        this.interactionListener = interactionListener;
    }

    @Override
    public void showMovie(Movie movie) {
        this.movie = movie;
        movieTextView.setText(getText(R.string.movie_title, movie.title, movie.getYear()));
        movieScoreArcProgress.setProgress(movie.getScore());
        movieGenreTextView.setText(movie.getGenres());
        movieOverviewTextView.setText(getText(R.string.overview, movie.overview));
        movieReleaseDateTextView.setText(getText(R.string.release_date, movie.getReleaseDate()));
        movieCastTextView.setVisibility(View.INVISIBLE);
        Glide.with(getContext())
                .load(movie.poster == null ? movie.getPosterThumbPath() : Uri.fromFile(new File(movie.poster)))
                .listener(new GlideListener(appDataBase, GlideListener.POSTER_TYPE, movie, getContext()))
                .apply(glideRequestOptions(getContext()))
                .into(posterImageView);
        Glide.with(getContext())
                .load(movie.backdrop == null ? movie.getBackdropPath() : Uri.fromFile(new File(movie.backdrop)))
                .listener(new GlideListener(appDataBase, GlideListener.BACKDROP_TYPE, movie, getContext()))
                .apply(glideRequestOptions(getContext()))
                .into(movieBackdropPathImageView);
        if(movie.getYoutubeVideo() != null){
            youtubeButton.setVisibility(View.VISIBLE);
        }else{
            youtubeButton.setVisibility(View.GONE);
        }
        if(movie.casts != null){
            actorAdapter.setCasts(movie.casts);
            movieCastTextView.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        onScrollChanged(scrollView.getCurrentScrollY(), false, false);
    }

    @Override
    public void onScrollChanged(int scrollY, boolean firstScroll, boolean dragging) {
        float alpha = Math.min(1, (float) scrollY / parallaxImageHeight);
        Animation fadeIn = AnimationUtils.loadAnimation(getContext(), R.anim.fade_in);
        Animation fadeOut = AnimationUtils.loadAnimation(getContext(), R.anim.fade_out);
        if(alpha >= 1 && favoriteVisible){
            favoriteVisible = false;
            favoriteFloatingActionButton.startAnimation(fadeOut);
        }
        if (alpha < 1 && !favoriteVisible){
            favoriteVisible = true;
            favoriteFloatingActionButton.startAnimation(fadeIn);
        }
        parallaxLayout.setTranslationY(scrollY / 2);
    }

    @Override
    public void onDownMotionEvent() {

    }

    @Override
    public void onUpOrCancelMotionEvent(ScrollState scrollState) {

    }

}
