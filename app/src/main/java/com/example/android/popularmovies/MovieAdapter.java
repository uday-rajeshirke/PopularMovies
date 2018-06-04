package com.example.android.popularmovies;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import com.squareup.picasso.Picasso;
import java.util.ArrayList;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieViewHolder> {

    private final ArrayList<Movie> mMovies;
    private final ListItemClickListener mOnClickListener;

    MovieAdapter(ArrayList<Movie> movies, ListItemClickListener clickListener) {
        this.mMovies = movies;
        this.mOnClickListener = clickListener;
    }

    public interface ListItemClickListener {
        void onListItemClick(Movie clickedMovieObject);
    }

    @NonNull
    @Override
    public MovieViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        int layoutIdForListItem = R.layout.movie_list_item;
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(layoutIdForListItem,parent, false);
        return new MovieViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MovieViewHolder holder, int position) {
        holder.listMovieObject = mMovies.get(position);
        holder.bind();
    }

    @Override
    public int getItemCount() {
        return mMovies.size();
    }

    public class MovieViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        Movie listMovieObject;
        final ImageView listMoviePosterView;

        MovieViewHolder(View itemView) {
            super(itemView);

            listMovieObject = new Movie();
            listMoviePosterView = itemView.findViewById(R.id.iv_view_holder_movie_poster);
            itemView.setOnClickListener(this);
        }

        void bind() {
            String moviePosterPath = String.valueOf(NetworkUtils.buildMoviePosterUrl(listMovieObject.getPosterPath(),1));
            Picasso.get().load(moviePosterPath).into(listMoviePosterView);
        }

        @Override
        public void onClick(View v) {
            mOnClickListener.onListItemClick(listMovieObject);
        }
    }


}
