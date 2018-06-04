package com.example.android.popularmovies;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;


public class MovieDetailsFragment extends Fragment {

    private static final String ARG_PARAM1 = "selectedMovie";
    private Movie mSelectedMovie;
    private TextView mMovieTitle, mMovieReleaseYear, mMovieRating, mMovieSynopsis;
    private ImageView mMoviePoster;
    private Toolbar mToolbar;

    public static MovieDetailsFragment newInstance(Movie selectedMovie) {
        MovieDetailsFragment fragment = new MovieDetailsFragment();
        Bundle args = new Bundle();
        args.putParcelable(ARG_PARAM1, selectedMovie);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        if (getArguments() != null) {
            mSelectedMovie = new Movie();
            mSelectedMovie = getArguments().getParcelable(ARG_PARAM1);
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_movie_details, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if(getActivity()!=null) {
            mToolbar = getActivity().findViewById(R.id.movie_details_toolbar);
            ((AppCompatActivity) getActivity()).setSupportActionBar(mToolbar);
            ActionBar mActionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
            if (mActionBar != null) {
                mActionBar.setDisplayHomeAsUpEnabled(true);
                mActionBar.setTitle("Movie Details");
            }
        }

        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager().popBackStack();
            }
        });

        mMovieTitle = view.findViewById(R.id.tv_movie_details_title);
        mMoviePoster = view.findViewById(R.id.iv_movie_details_poster);
        mMovieReleaseYear = view.findViewById(R.id.tv_movie_details_release_year);
        mMovieRating = view.findViewById(R.id.tv_movie_details_rating);
        mMovieSynopsis = view.findViewById(R.id.tv_movie_details_synopsis);

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

    }

    @Override
    public void onResume() {
        super.onResume();
        mMovieTitle.setText(mSelectedMovie.getOriginalTitle());
        mMovieRating.append(mSelectedMovie.getVoteAverage() + "/10");
        mMovieSynopsis.setText(mSelectedMovie.getOverview());

        try {
            Date formattedReleaseDate = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                    .parse(mSelectedMovie.getReleaseDate());
            SimpleDateFormat formatOnlyYear = new SimpleDateFormat("yyyy", Locale.getDefault());
            mMovieReleaseYear.setText(formatOnlyYear.format(formattedReleaseDate));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        String moviePosterPath = String.valueOf(NetworkUtils.buildMoviePosterUrl(mSelectedMovie.getPosterPath(),2));
        Picasso.get().load(moviePosterPath).into(mMoviePoster);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.clear();
    }
}
