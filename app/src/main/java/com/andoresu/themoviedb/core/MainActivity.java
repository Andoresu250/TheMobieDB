package com.andoresu.themoviedb.core;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.andoresu.themoviedb.R;
import com.andoresu.themoviedb.core.authorization.LoginDialogFragment;
import com.andoresu.themoviedb.core.authorization.data.Session;
import com.andoresu.themoviedb.core.authorization.data.User;
import com.andoresu.themoviedb.core.moviedetail.MovieDetailContract;
import com.andoresu.themoviedb.core.moviedetail.MovieDetailFragment;
import com.andoresu.themoviedb.core.movies.MoviesContract;
import com.andoresu.themoviedb.core.movies.MoviesFragment;
import com.andoresu.themoviedb.core.movies.data.Movie;
import com.andoresu.themoviedb.utils.BaseActivity;
import com.andoresu.themoviedb.utils.BaseFragment;

import butterknife.BindView;
import butterknife.ButterKnife;

import com.andoresu.themoviedb.utils.MyUtils;
import com.bumptech.glide.Glide;
import com.crashlytics.android.Crashlytics;
import io.fabric.sdk.android.Fabric;

import static com.andoresu.themoviedb.utils.MyUtils.glideRequestOptions;

public class MainActivity extends BaseActivity implements
        NavigationView.OnNavigationItemSelectedListener,
        MainContract.View, MoviesContract.InteractionListener,
        MovieDetailContract.InteractionListener,
        LoginDialogFragment.InteractionListener {

    public String TAG = "THEMOVIEDB_" + MainActivity.class.getSimpleName();

    @BindView(R.id.toolbar)
    public Toolbar toolbar;

    @BindView(R.id.drawer_layout)
    DrawerLayout drawer;

    @BindView(R.id.nav_view)
    NavigationView navigationView;

    private MainContract.ActionsListener actionsListener;

    private HeaderViewHolder headerViewHolder;

    private User user;

    private BaseFragment currentFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fabric.with(this, new Crashlytics());
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);

        setTitle(R.string.app_name);
        headerViewHolder = new HeaderViewHolder(navigationView.getHeaderView(0));

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        navigationView.setNavigationItemSelectedListener(this);

        actionsListener = new MainPresenter(this, this);

        setMoviesFragment();

        init();

    }

    private void setUserNameToMenu() {
        if(user == null){
            headerViewHolder.userNameTextView.setText(R.string.guest);
            headerViewHolder.userEmailTextView.setText(" ");
            headerViewHolder.userImageView.setImageResource(R.mipmap.ic_launcher_round);
        }else{
            headerViewHolder.userNameTextView.setText(user.username);
            headerViewHolder.userEmailTextView.setText(user.name);
            Glide.with(this)
                    .load(user.getImage())
                    .apply(glideRequestOptions(true))
                    .into(headerViewHolder.userImageView);
        }
    }

    private void init(){
        Menu navMenu = navigationView.getMenu();
        MenuItem navLogin = navMenu.findItem(R.id.nav_login);
        MenuItem navProfile = navMenu.findItem(R.id.nav_profile);
        MenuItem navLogout = navMenu.findItem(R.id.nav_logout);
        if(user == null){
            navLogin.setVisible(true);
            navProfile.setVisible(false);
            navLogout.setVisible(false);
        }else{
            navLogin.setVisible(false);
            navProfile.setVisible(true);
            navLogout.setVisible(true);
        }
        setUserNameToMenu();
        currentFragment.user = user;
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        switch (id){
            case R.id.nav_movies:
                setMoviesFragment();
                break;
            case R.id.nav_favorites:
                if(user == null){
                    loginRequired();
                }else{
                    setFavoritesFragment();
                }
                break;
            case R.id.nav_login:
                openLoginDialog();
                break;
            case R.id.nav_profile:

                break;
            case R.id.nav_logout:
                actionsListener.logout(new Session(this.user.sessionId));
                this.user = null;
                init();
                break;
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void openLoginDialog(){
        LoginDialogFragment loginDialogFragment = LoginDialogFragment.newInstance(this);
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.addToBackStack(null);
        loginDialogFragment.show(fragmentTransaction);
    }

    private void changeFragment(BaseFragment fragment){
        currentFragment = fragment;
        currentFragment.user = user;
        fragment.mainView = this;
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.mainFragment, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    private void setMoviesFragment(){
        MoviesFragment moviesFragment = MoviesFragment.newInstance(this, false);
        changeFragment(moviesFragment);
    }

    private void setFavoritesFragment(){
        MoviesFragment moviesFragment = MoviesFragment.newInstance(this, true);
        changeFragment(moviesFragment);
    }

    @Override
    public void movieDetail(Movie movie) {
        MovieDetailFragment movieDetailFragment = MovieDetailFragment.newInstance(this, movie);
        changeFragment(movieDetailFragment);
    }

    @Override
    public void setUser(User user) {
        this.user = user;
        init();
    }

    @Override
    public void loginRequired(){
        MyUtils.showDialog(this, "Login Required", "to do this action you need to be loged", getDrawable(R.drawable.ic_user), R.color.colorAccent,
                (dialogInterface, i) -> openLoginDialog(), "Login",
                null, "Cancel");
    }

    protected static class HeaderViewHolder {

        @BindView(R.id.userNameTextView)
        TextView userNameTextView;

        @BindView(R.id.userEmailTextView)
        TextView userEmailTextView;

        @BindView(R.id.userImageView)
        ImageView userImageView;

        HeaderViewHolder(View view) {
            ButterKnife.bind(this, view);
        }

    }
}
