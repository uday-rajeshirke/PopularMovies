package com.example.android.popularmovies;

import android.os.Parcel;
import android.os.Parcelable;
import java.util.ArrayList;
import java.util.Collections;

class Movie implements Parcelable {

    private int mVoteCount;
    private int mId;
    private boolean mVideo;
    private double mVoteAverage;
    private String mTitle;
    private double mPopularity;
    private String mPosterPath;
    private String mOriginalLanguage;
    private String mOriginalTitle;
    private ArrayList<Integer> mGenreIds = null;
    private String mBackdropPath;
    private boolean mAdult;
    private String mOverview;
    private String mReleaseDate;

    Movie() {
    }

    private Movie(int voteCount, int id, boolean video, double voteAverage, String title,
                  double popularity, String posterPath, String originalLanguage, String originalTitle,
                  ArrayList<Integer> genreIds, String backdropPath, boolean adult, String overview, String releaseDate) {
        this.mVoteCount = voteCount;
        this.mId = id;
        this.mVideo = video;
        this.mVoteAverage = voteAverage;
        this.mTitle = title;
        this.mPopularity = popularity;
        this.mPosterPath = posterPath;
        this.mOriginalLanguage = originalLanguage;
        this.mOriginalTitle = originalTitle;
        this.mGenreIds = genreIds;
        this.mBackdropPath = backdropPath;
        this.mAdult = adult;
        this.mOverview = overview;
        this.mReleaseDate = releaseDate;
    }

    public int getVoteCount() {
        return mVoteCount;
    }

    public void setVoteCount(int voteCount) {
        this.mVoteCount = voteCount;
    }

    public int getId() {
        return mId;
    }

    public void setId(int id) {
        this.mId = id;
    }

    public boolean isVideo() {
        return mVideo;
    }

    public void setVideo(boolean video) {
        this.mVideo = video;
    }

    public double getVoteAverage() {
        return mVoteAverage;
    }

    public void setVoteAverage(double voteAverage) {
        this.mVoteAverage = voteAverage;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        this.mTitle = title;
    }

    public double getPopularity() {
        return mPopularity;
    }

    public void setPopularity(double popularity) {
        this.mPopularity = popularity;
    }

    public String getPosterPath() {
        return mPosterPath;
    }

    public void setPosterPath(String posterPath) {
        this.mPosterPath = posterPath;
    }

    public String getOriginalLanguage() {
        return mOriginalLanguage;
    }

    public void setOriginalLanguage(String originalLanguage) {
        this.mOriginalLanguage = originalLanguage;
    }

    public String getOriginalTitle() {
        return mOriginalTitle;
    }

    public void setOriginalTitle(String originalTitle) {
        this.mOriginalTitle = originalTitle;
    }

    private ArrayList<Integer> getGenreIds() {
        return mGenreIds;
    }

    public void setGenreIds(ArrayList<Integer> genreIds) {
        this.mGenreIds = genreIds;
    }

    public String getBackdropPath() {
        return mBackdropPath;
    }

    public void setBackdropPath(String backdropPath) {
        this.mBackdropPath = backdropPath;
    }

    public boolean isAdult() {
        return mAdult;
    }

    public void setAdult(boolean adult) {
        this.mAdult = adult;
    }

    public String getOverview() {
        return mOverview;
    }

    public void setOverview(String overview) {
        this.mOverview = overview;
    }

    public String getReleaseDate() {
        return mReleaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.mReleaseDate = releaseDate;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    private Movie(Parcel in){

        int[] intData = new int[2];
        boolean[] booleanData = new boolean[2];
        double[] doubleData = new double[2];
        String[] stringData1 = new String[4];
        String[] stringData2 = new String[2];
        ArrayList listData;

        in.readIntArray(intData);
        in.readBooleanArray(booleanData);
        in.readDoubleArray(doubleData);
        in.readStringArray(stringData1);
        in.readStringArray(stringData2);
        listData = in.readArrayList(Movie.class.getClassLoader());

        this.mVoteCount = intData[0];
        this.mId = intData[1];
        this.mVideo = booleanData[0];
        this.mVoteAverage = doubleData[0];
        this.mTitle = stringData1[0];
        this.mPopularity = doubleData[1];
        this.mPosterPath = stringData2[0];
        this.mOriginalLanguage = stringData1[1];
        this.mOriginalTitle = stringData1[2];
        Collections.copy(this.mGenreIds,listData);
        this.mBackdropPath = stringData2[1];
        this.mAdult = booleanData[1];
        this.mOverview = in.readString();
        this.mReleaseDate = stringData1[3];
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeIntArray(new int[]{mVoteCount,mId});
        dest.writeBooleanArray(new boolean[]{mVideo,mAdult});
        dest.writeDoubleArray(new double[] {mVoteAverage,mPopularity});
        dest.writeStringArray(new String[] {mTitle,mOriginalLanguage,mOriginalTitle,mReleaseDate});
        dest.writeStringArray(new String[] {mPosterPath,mBackdropPath});
        dest.writeString(mOverview);
        dest.writeList(getGenreIds());
    }

    public static final Creator<Movie> CREATOR = new Creator<Movie>() {
        @Override
        public Movie createFromParcel(Parcel in) {
            return new Movie(in);
        }

        @Override
        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };
}
