package com.example.muthuveerappans.course3.Activities;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.muthuveerappans.course3.Modals.Genre;
import com.example.muthuveerappans.course3.NetworkHelpers.DownloadCallback;
import com.example.muthuveerappans.course3.NetworkHelpers.NetworkFragment;
import com.example.muthuveerappans.course3.NetworkHelpers.NetworkObject;
import com.example.muthuveerappans.course3.NetworkHelpers.Result;
import com.example.muthuveerappans.course3.ParserHelpers.JsonToPojoParser;
import com.example.muthuveerappans.course3.R;
import com.example.muthuveerappans.course3.RecyclerItemClickListener;
import com.example.muthuveerappans.course3.Specs;

import java.util.ArrayList;
import java.util.List;

public class GenresListActivity extends AppCompatActivity implements DownloadCallback<Result> {
    private RecyclerView genreRecyclerView;
    private List<Genre> genres = new ArrayList<>(); // holds the genre data.
    private GenreAdapter genreAdapter;

    private NetworkFragment networkFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.movies_list);

        genreRecyclerView = (RecyclerView) findViewById(R.id.movies_list_view);

        // set the layout manager for Recycler View
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        genreRecyclerView.setLayoutManager(layoutManager);

        // set the data adapter for this recyclerview
        genreAdapter = new GenreAdapter();
        genreRecyclerView.setAdapter(genreAdapter);

        // get the data from server.
        getData();

        // set the item click listener for our recyclerview
        genreRecyclerView.addOnItemTouchListener(new RecyclerItemClickListener(this
                , new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Genre genre = genres.get(position);

                // start the next activity to show all the movies in this genre
                startNextActivity(genre);
            }
        }));
    }

    // get the data from server using HTTP.
    private void getData() {
        if (networkFragment == null) {
            String url = Specs.genreListURL();
            NetworkObject networkObject = new NetworkObject(url, NetworkObject.HTTP_METHOD.GET);
            networkFragment = NetworkFragment.getInstance(getSupportFragmentManager()
                    , networkObject);
        }

        networkFragment.startDownload();
    }

    // Helper to start the next activity that shows all the movies in this particular genre.
    private void startNextActivity(Genre genre) {
        Intent intent = new Intent(this, MoviesListActivity.class);
        intent.putExtra("genre_data", genre);
        startActivity(intent);
    }

    /**
     * Below methods are inherited from the interface {@link DownloadCallback}.
     */

    @Override
    public void onResponseReceived(Result result) {
        if (genreRecyclerView != null && genreAdapter != null) {
            genres.clear(); // clear the data list so we don't append the values. We need to add a new set of values

            if (result.mException != null) {
                Toast.makeText(GenresListActivity.this, result.mException.getLocalizedMessage(), Toast.LENGTH_SHORT)
                        .show();
            } else if (result.mResultValue != null) {
                genres.addAll(JsonToPojoParser.getInstance().getGenresFromJson(result.mResultValue));
            }

            genreAdapter.notifyDataSetChanged();
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
        // NOT IMPLEMENTED
    }

    @Override
    public void onFinish() {
        if (networkFragment != null) {
            networkFragment.cancelDownload();
        }
    }

    private class GenreAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View rootView = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_genere, parent, false);
            return new GenreViewHolder(rootView);
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            GenreViewHolder genreViewHolder = (GenreViewHolder) holder;
            Genre genre = genres.get(position);
            genreViewHolder.setGenre(genre.getName());
        }

        @Override
        public int getItemCount() {
            return genres.size();
        }
    }

    private static class GenreViewHolder extends RecyclerView.ViewHolder {
        private TextView movie_genre_txt;

        GenreViewHolder(View itemView) {
            super(itemView);

            movie_genre_txt = (TextView) itemView.findViewById(R.id.movie_genre_txt);
        }

        void setGenre(String genre) {
            movie_genre_txt.setText(genre);
        }
    }
}
