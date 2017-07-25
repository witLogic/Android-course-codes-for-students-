package com.example.muthuveerappans.course3.Activities;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.muthuveerappans.course3.Modals.Genre;
import com.example.muthuveerappans.course3.Modals.Movie;
import com.example.muthuveerappans.course3.NetworkHelpers.DownloadCallback;
import com.example.muthuveerappans.course3.NetworkHelpers.NetworkFragment;
import com.example.muthuveerappans.course3.NetworkHelpers.NetworkObject;
import com.example.muthuveerappans.course3.NetworkHelpers.Result;
import com.example.muthuveerappans.course3.ParserHelpers.JsonToPojoParser;
import com.example.muthuveerappans.course3.R;
import com.example.muthuveerappans.course3.Specs;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by muthuveerappans on 7/25/17.
 */

public class MoviesListActivity extends AppCompatActivity implements DownloadCallback<Result> {
    private RecyclerView moviesListRecyclerView;
    private List<Movie> movieList = new ArrayList<>();
    private MoviesAdapter moviesAdapter;
    private NetworkFragment networkFragment;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.movies_list);

        Genre genre = getIntent().getExtras().getParcelable("genre_data");

        moviesListRecyclerView = (RecyclerView) findViewById(R.id.movies_list_view);

        // set the layout manager for Recycler View
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        moviesListRecyclerView.setLayoutManager(layoutManager);

        // set the data adapter
        moviesAdapter = new MoviesAdapter();
        moviesListRecyclerView.setAdapter(moviesAdapter);

        if (genre != null) {
            TextView movie_description_txt = (TextView) findViewById(R.id.movie_description_txt);
            movie_description_txt.setText(genre.getName());
            getData(genre.getId());
        }
    }

    // get the data from server using HTTP.
    private void getData(int genreID) {
        if (networkFragment == null) {
            String url = Specs.genreMovieListForGenreURL(genreID);
            NetworkObject networkObject = new NetworkObject(url, NetworkObject.HTTP_METHOD.GET);
            networkFragment = NetworkFragment.getInstance(getSupportFragmentManager()
                    , networkObject);
        }

        networkFragment.startDownload();
    }

    @Override
    public void onResponseReceived(Result result) {
        if (moviesListRecyclerView != null && moviesAdapter != null) {
            movieList.clear();

            if (result.mException != null) {
                Toast.makeText(this, result.mException.getLocalizedMessage(), Toast.LENGTH_SHORT)
                        .show();
            } else if (result.mResultValue != null) {
                movieList.addAll(JsonToPojoParser.getInstance().getMoviesFromJson(result.mResultValue));
            }

            moviesAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public NetworkInfo getActiveNetworkInfo() {
        ConnectivityManager connectivityManager =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return networkInfo;
    }

    @Override
    public void onProgressUpdate(int progressCode, int percentComplete) {

    }

    @Override
    public void onFinish() {
        if (networkFragment != null) {
            networkFragment.cancelDownload();
        }
    }

    private class MoviesAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View rootView = LayoutInflater.from(parent.getContext()).inflate(R.layout.movie_list_card, parent, false);
            return new MoviesViewHolder(rootView);
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            MoviesViewHolder moviesViewHolder = (MoviesViewHolder) holder;
            Movie movie = movieList.get(position);
            moviesViewHolder.setMovies(movie.getPosterPath(),
                    movie.getTitle(),
                    movie.getDescription(),
                    movie.getVoteAverage());
        }

        @Override
        public int getItemCount() {
            return movieList.size();
        }
    }

    private static class MoviesViewHolder extends RecyclerView.ViewHolder {
        private ImageView movie_image;
        private TextView movie_title_txt;
        private TextView movie_description_txt;
        private TextView movie_rating_txt;

        private Context context;

        MoviesViewHolder(View itemView) {
            super(itemView);

            context = itemView.getContext();

            movie_image = (ImageView) itemView.findViewById(R.id.movie_image);
            movie_title_txt = (TextView) itemView.findViewById(R.id.movie_title_txt);
            movie_description_txt = (TextView) itemView.findViewById(R.id.movie_description_txt);
            movie_rating_txt = (TextView) itemView.findViewById(R.id.movie_rating_txt);
        }

        void setMovies(String imageURL, String movieTitle, String movieDescription, float movieRating) {
            movie_title_txt.setText(movieTitle);
            movie_description_txt.setText(movieDescription);
            movie_rating_txt.setText(String.valueOf(movieRating));

            // Use the library Picasso to asynchronously load image data from the server.
            // For more information visit http://square.github.io/picasso/
            String fullImageURL = Specs.getMoviePosterURL(imageURL);
            Picasso.with(context).load(fullImageURL).resize(500, 800).centerCrop().into(movie_image);
        }
    }
}
