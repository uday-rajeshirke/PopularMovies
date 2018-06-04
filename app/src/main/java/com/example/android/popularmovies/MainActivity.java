package com.example.android.popularmovies;

import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.AsyncTask;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements MovieAdapter.ListItemClickListener, SharedPreferences.OnSharedPreferenceChangeListener {

    private RecyclerView mMoviesRecyclerView;
    private ProgressBar mLoadingIndicator;
    private PreferencesHelper mPreferencesHelper;
    private RelativeLayout mMovieListLayout;
    private LinearLayout mNoConnectionLinearLayout;
    private Snackbar mSnackbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mMovieListLayout = findViewById(R.id.layout_movie_list);
        mNoConnectionLinearLayout = findViewById(R.id.ll_no_connection);
        Toolbar mToolbar = findViewById(R.id.main_toolbar);
        setSupportActionBar(mToolbar);

        mMoviesRecyclerView = findViewById(R.id.rv_movies);
        mLoadingIndicator = findViewById(R.id.pb_loading_indicator);

        mPreferencesHelper = new PreferencesHelper(getApplicationContext());
        mPreferencesHelper.getSharedPreferences().registerOnSharedPreferenceChangeListener(this);

        GridLayoutManager layoutManager;
        int mScreenOrientation = this.getResources().getConfiguration().orientation;

        switch (mScreenOrientation) {

            case Configuration.ORIENTATION_PORTRAIT:
                layoutManager = new GridLayoutManager(this,2);
                break;
            case Configuration.ORIENTATION_LANDSCAPE:
                layoutManager = new GridLayoutManager(this,3);
                break;
            default:
                layoutManager = null;
        }

        if(layoutManager != null)
            mMoviesRecyclerView.setLayoutManager(layoutManager);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(NetworkUtils.isOnline(MainActivity.this)) {
            mNoConnectionLinearLayout.setVisibility(View.GONE);
            new TheMovieDbAsyncTask().execute(mPreferencesHelper.getSortingOptionPreferences("SelectedSortingOption"));
        }
        else
            noConnectionDialog();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int menuItemSelected = item.getItemId();

        if(menuItemSelected == R.id.action_setting) {
            final SortingOptionListDialogFragment optionListDialogFragment = SortingOptionListDialogFragment.newInstance();
            optionListDialogFragment.setOptionListener(new SortingOptionListDialogFragment.SortingOptionListener() {
                @Override
                public void onSortingOptionClicked(int option) {
                    mPreferencesHelper.saveSortingOptionPreferences("SelectedSortingOption",option);
                    optionListDialogFragment.dismiss();
                }
            });
            optionListDialogFragment.show(getSupportFragmentManager(),"Sort options");
        }
        return true;
    }

    @Override
    public void onListItemClick(Movie clickedMovieObject) {
        MovieDetailsFragment movieDetailsFragment = MovieDetailsFragment.newInstance(clickedMovieObject);
        getSupportFragmentManager().beginTransaction().replace(R.id.main_container,movieDetailsFragment).addToBackStack(null).commit();
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {

        if(key.equals("SelectedSortingOption")) {
            if(NetworkUtils.isOnline(MainActivity.this)) {
                mNoConnectionLinearLayout.setVisibility(View.GONE);
                new TheMovieDbAsyncTask().execute(mPreferencesHelper.getSortingOptionPreferences("SelectedSortingOption"));
            }
            else
                noConnectionDialog();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPreferencesHelper.getSharedPreferences().unregisterOnSharedPreferenceChangeListener(this);
    }

    private class TheMovieDbAsyncTask extends AsyncTask<Integer, Void, ArrayList<Movie>> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mLoadingIndicator.setVisibility(View.VISIBLE);
        }

        @Override
        protected ArrayList<Movie> doInBackground(Integer... integers) {

            URL movieUrl = NetworkUtils.buildUrl(integers[0]);
            try {
               return NetworkUtils.getResponseFromHttpUrl(movieUrl);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(ArrayList<Movie> movies) {
            super.onPostExecute(movies);
            mLoadingIndicator.setVisibility(View.INVISIBLE);
            if(!movies.isEmpty()) {
                MovieAdapter mMovieAdapter = new MovieAdapter(movies, MainActivity.this);
                mMoviesRecyclerView.setAdapter(mMovieAdapter);
            }
        }
    }

    private void noConnectionDialog() {
        mNoConnectionLinearLayout.setVisibility(View.VISIBLE);
        mSnackbar = Snackbar.make(mMovieListLayout,"No Internet connection",Snackbar.LENGTH_INDEFINITE);
        mSnackbar.setAction("RETRY", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(NetworkUtils.isOnline(MainActivity.this)) {
                    mNoConnectionLinearLayout.setVisibility(View.GONE);
                    mSnackbar.dismiss();
                    new TheMovieDbAsyncTask().execute(mPreferencesHelper.getSortingOptionPreferences("SelectedSortingOption"));
                }
                else {
                    noConnectionDialog();
                }
            }
        });
        mSnackbar.show();
    }
}
