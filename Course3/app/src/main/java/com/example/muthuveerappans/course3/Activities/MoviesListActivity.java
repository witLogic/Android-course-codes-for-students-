package com.example.muthuveerappans.course3.Activities;

import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.example.muthuveerappans.course3.Modals.Genre;
import com.example.muthuveerappans.course3.NetworkHelpers.DownloadCallback;
import com.example.muthuveerappans.course3.NetworkHelpers.NetworkFragment;
import com.example.muthuveerappans.course3.NetworkHelpers.NetworkObject;
import com.example.muthuveerappans.course3.NetworkHelpers.Result;
import com.example.muthuveerappans.course3.R;
import com.example.muthuveerappans.course3.Specs;

/**
 * Created by muthuveerappans on 7/25/17.
 */

public class MoviesListActivity extends AppCompatActivity implements DownloadCallback<Result> {
    private RecyclerView moviesListRecyclerView;

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

    }

    @Override
    public NetworkInfo getActiveNetworkInfo() {
        return null;
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
}
