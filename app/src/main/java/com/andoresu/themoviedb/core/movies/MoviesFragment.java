package com.andoresu.themoviedb.core.movies;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.andoresu.themoviedb.R;
import com.andoresu.themoviedb.core.movies.data.Movies;
import com.andoresu.themoviedb.utils.BaseFragment;
import com.andoresu.themoviedb.utils.PaginationScrollListener;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MoviesFragment extends BaseFragment implements MoviesContract.View, SwipeRefreshLayout.OnRefreshListener{

    String TAG = "MOVIEDB_" + MoviesFragment.class.getSimpleName();

    private static final int PAGE_START = 1;
    private boolean isLoading = false;

    @BindView(R.id.moviesRecyclerView)
    RecyclerView moviesRecyclerView;

    @BindView(R.id.moviesSwipeRefreshLayout)
    SwipeRefreshLayout swipeRefreshLayout;

    @BindView(R.id.emptyListTextView)
    TextView emptyListTextView;

    @BindView(R.id.reloadImageButton)
    ImageButton reloadImageButton;

    private MoviesContract.ActionsListener actionsListener;

    private MoviesContract.InteractionListener interactionListener;

    private LinearLayoutManager linearLayoutManager;

    private int currentPage = PAGE_START;

    private MovieAdapter movieAdapter;

    private boolean getFavorites = false;

    public MoviesFragment(){}

    public static MoviesFragment newInstance(MoviesContract.InteractionListener interactionListener, boolean getFavorites) {
        Bundle args = new Bundle();
        MoviesFragment fragment = new MoviesFragment();
        fragment.setArguments(args);
        fragment.setInteractionListener(interactionListener);
        fragment.setGetFavorites(getFavorites);
        return fragment;
    }

    private void setGetFavorites(boolean getFavorites) {
        this.getFavorites = getFavorites;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        if(bundle != null){}
        this.actionsListener = new MoviesPresenter(this, getContext());
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_movies, container, false);
        setUnbinder(ButterKnife.bind(this, view));

        swipeRefreshLayout.setColorSchemeResources(R.color.colorAccent);
        swipeRefreshLayout.setOnRefreshListener(this);
        linearLayoutManager = new LinearLayoutManager(this.getContext(), 1, false);
        moviesRecyclerView.setLayoutManager(linearLayoutManager);
        movieAdapter = new MovieAdapter(getContext(), item -> interactionListener.movieDetail(item));
        moviesRecyclerView.setAdapter(movieAdapter);
        moviesRecyclerView.addOnScrollListener(getPaginationScrollListener());
        getMovies();
        reloadImageButton.setOnClickListener(view1 -> getMovies());
        return view;
    }

    private void setInteractionListener(MoviesContract.InteractionListener interactionListener) {
        this.interactionListener = interactionListener;
    }

    private Map<String, String> getMoviesOptions(){
        Map<String, String> options = new HashMap<>();
        options.put("page", currentPage + "");
        options.put("sort_by", "popularity.desc");
        return options;
    }

    @Override
    public void onRefresh() {
        getMovies();
    }

    @Override
    public void showMovies(Movies movies) {
        if(movies == null || movies.results == null || movies.results.isEmpty()){
            emptyListTextView.setVisibility(View.VISIBLE);
            reloadImageButton.setVisibility(View.VISIBLE);
            moviesRecyclerView.setVisibility(View.INVISIBLE);
            return;
        }
        emptyListTextView.setVisibility(View.INVISIBLE);
        reloadImageButton.setVisibility(View.INVISIBLE);
        moviesRecyclerView.setVisibility(View.VISIBLE);
        if(this.movieAdapter.movies == null){
            this.movieAdapter.setMovies(movies);
        }else{
            this.movieAdapter.addAll(movies.results);
        }

    }

    private void getMovies(){
        if(getFavorites){
            actionsListener.getFavoritesMovies(getMoviesOptions(), user);
        }else{
            actionsListener.getMovies(getMoviesOptions());
        }
    }

    @Override
    public void showProgressIndicator(boolean active) {
        super.showProgressIndicator(active);
        swipeRefreshLayout.setRefreshing(active);
        isLoading = active;
    }

    private PaginationScrollListener getPaginationScrollListener(){
        return new PaginationScrollListener(linearLayoutManager) {
            @Override
            protected void loadMoreItems() {
                showProgressIndicator(true);
                currentPage++;
                getMovies();
            }

            @Override
            public int getTotalPageCount() {
                return movieAdapter.movies.totalPages;
            }

            @Override
            public boolean isLastPage() {
                return currentPage >= movieAdapter.movies.totalPages;
            }

            @Override
            public boolean isLoading() {
                return isLoading;
            }
        };
    }
}
