package com.example.android.popularmovies;


import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

class NetworkUtils {

    private static final String TMDB_BASE_URL = "https://api.themoviedb.org/3/movie/";
    private final static String MOVIE_POSTER_BASE_URL =
            "https://image.tmdb.org/t/p/";
    private static final String PARAM_KEY = "api_key";
    // TODO Enter your API key here
    private static final String TMDB_API_KEY = "your api key";
    private static final String PATH_TOP_RATED = "top_rated";
    private static final String PATH_POPULAR = "popular";
    private static final String IMAGE_SIZE_1 = "w500";
    private static final String IMAGE_SIZE_2 = "w185";

    public static URL buildUrl(int sortBy) {

        URL url = null;
        String selectedOption;

        switch(sortBy) {

            case 1:
                selectedOption = PATH_POPULAR;
                break;
            case 2:
                selectedOption = PATH_TOP_RATED;
                break;
            default:
                selectedOption = null;
                break;
        }

        if(selectedOption != null) {

            Uri builtUri = Uri.parse(TMDB_BASE_URL)
                    .buildUpon()
                    .appendPath(selectedOption)
                    .appendQueryParameter(PARAM_KEY, TMDB_API_KEY)
                    .build();

            try {
                url = new URL(builtUri.toString());
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
        }

        return url;
    }

    public static ArrayList<Movie> getResponseFromHttpUrl(URL url) throws IOException {

        ArrayList<Movie> responseMovieList = new ArrayList<>();
        HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
        StringBuilder stringBuilder;

        try {

            InputStream in = new BufferedInputStream(httpURLConnection.getInputStream());
            stringBuilder = new StringBuilder();
            BufferedReader reader = new BufferedReader(new InputStreamReader(in));
            String line;
            while ((line = reader.readLine()) != null) {
                stringBuilder.append(line);
            }

        } finally {
            httpURLConnection.disconnect();
        }

        try {

            JSONObject movieResponse = new JSONObject(stringBuilder.toString());
            JSONArray resultArray = movieResponse.getJSONArray("results");
            for(int i=0; i<resultArray.length(); i++) {

                JSONObject movieJSONObject = resultArray.getJSONObject(i);
                Movie movieObject = new Movie();

                movieObject.setVoteCount(movieJSONObject.getInt("vote_count"));
                movieObject.setId(movieJSONObject.getInt("id"));
                movieObject.setVideo(movieJSONObject.getBoolean("video"));
                movieObject.setVoteAverage(movieJSONObject.getDouble("vote_average"));
                movieObject.setTitle(movieJSONObject.getString("title"));
                movieObject.setPopularity(movieJSONObject.getDouble("popularity"));
                movieObject.setPosterPath(movieJSONObject.getString("poster_path"));
                movieObject.setOriginalLanguage(movieJSONObject.getString("original_language"));
                movieObject.setOriginalTitle(movieJSONObject.getString("original_title"));
                movieObject.setBackdropPath(movieJSONObject.getString("backdrop_path"));
                movieObject.setAdult(movieJSONObject.getBoolean("adult"));
                movieObject.setOverview(movieJSONObject.getString("overview"));
                movieObject.setReleaseDate(movieJSONObject.getString("release_date"));

                ArrayList<Integer> genreIdList = new ArrayList<>();
                JSONArray genreIdArray = movieJSONObject.getJSONArray("genre_ids");

                for (int j = 0; j < genreIdArray.length(); j++) {
                    genreIdList.add(genreIdArray.getInt(j));
                }
                movieObject.setGenreIds(genreIdList);
                responseMovieList.add(movieObject);
            }
            return responseMovieList;

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static URL buildMoviePosterUrl(String posterPath, int imageSizeType) {

        URL url = null;
        String selectedOption;

        switch(imageSizeType) {

            case 1:
                selectedOption = IMAGE_SIZE_1;
                break;
            case 2:
                selectedOption = IMAGE_SIZE_2;
                break;
            default:
                selectedOption = null;
                break;
        }

        if(selectedOption != null) {

            Uri builtUri = Uri.parse(MOVIE_POSTER_BASE_URL)
                    .buildUpon()
                    .appendEncodedPath(selectedOption)
                    .appendEncodedPath(posterPath)
                    .build();

            try {
                url = new URL(builtUri.toString());
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
        }

        return url;
    }

    public static boolean isOnline(Context context) {
        ConnectivityManager cm =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = null;
        if (cm != null) {
            netInfo = cm.getActiveNetworkInfo();
        }
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }

}
