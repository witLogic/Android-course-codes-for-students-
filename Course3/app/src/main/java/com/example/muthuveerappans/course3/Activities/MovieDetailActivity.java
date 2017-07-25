package com.example.muthuveerappans.course3.Activities;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.muthuveerappans.course3.Modals.Movie;
import com.example.muthuveerappans.course3.NetworkHelpers.DownloadCallback;
import com.example.muthuveerappans.course3.NetworkHelpers.NetworkFragment;
import com.example.muthuveerappans.course3.NetworkHelpers.NetworkObject;
import com.example.muthuveerappans.course3.NetworkHelpers.Result;
import com.example.muthuveerappans.course3.ParserHelpers.JsonToPojoParser;
import com.example.muthuveerappans.course3.R;
import com.example.muthuveerappans.course3.Specs;
import com.google.gson.JsonObject;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

/**
 * Created by muthuveerappans on 7/26/17.
 */

public class MovieDetailActivity extends AppCompatActivity implements DownloadCallback<Result> {
    ImageView favourite_image;
    NetworkFragment networkFragment;

    String accountDetailsURL;
    String markFavouriteURL;

    Movie movie; // the movie data.

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.movie_details_activity);

        ActionBar actionBar = getSupportActionBar();

        if (actionBar != null)
            actionBar.setDisplayHomeAsUpEnabled(true);

        movie = getIntent().getParcelableExtra("movie_data");

        ImageView movie_poster = (ImageView) findViewById(R.id.movie_poster);
        TextView movie_title_txt = (TextView) findViewById(R.id.movie_title_txt);
        TextView movie_description_txt = (TextView) findViewById(R.id.movie_description_txt);
        favourite_image = (ImageView) findViewById(R.id.favourite_image);
        favourite_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String sessionID = getSharedPreferences(Specs.prefsFileName, MODE_PRIVATE)
                        .getString(Specs.sessionIdPrefsKey, "");

                // if the session is empty, open the login page
                if (sessionID.isEmpty()) {
                    Intent intent = new Intent(MovieDetailActivity.this, LoginActivity.class);
                    startActivity(intent);
                } else {
                    // set the required urls
                    accountDetailsURL = Specs.accountDetailsURL(sessionID);
                    getAccountData();
                }
            }
        });
        favourite_image.setImageResource(R.mipmap.ic_heart_un_filled);

        // Use the library Picasso to asynchronously load image data from the server.
        // For more information visit http://square.github.io/picasso/
        String fullImageURL = Specs.getMoviePosterURL(movie.getPosterPath());
        Picasso.with(this).load(fullImageURL).resize(500, 800).centerCrop().into(movie_poster);

        movie_description_txt.setText(movie.getDescription());
        movie_title_txt.setText(movie.getTitle());
    }

    // This method is called if this activity is called with the filter FLAG_ACTIVITY_CLEAR_TOP.
    // Instead of starting from onCreate(Bundle), this is called.
    // More info: https://developer.android.com/reference/android/app/Activity.html#onNewIntent(android.content.Intent)
    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

        String sessionID = getSharedPreferences(Specs.prefsFileName, MODE_PRIVATE)
                .getString(Specs.sessionIdPrefsKey, "");

        // set the required urls
        accountDetailsURL = Specs.accountDetailsURL(sessionID);
        getAccountData();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onResponseReceived(Result result) {
        if (favourite_image == null) {
            return;
        }

        if (result.mResultValue != null) {
            if (result.url.equals(accountDetailsURL)) {
                int accountID = JsonToPojoParser.getInstance().getAccountID(result.mResultValue);

                if (accountID > 0) {
                    String sessionID = getSharedPreferences(Specs.prefsFileName, MODE_PRIVATE)
                            .getString(Specs.sessionIdPrefsKey, "");
                    // We have got the account id. Using that post favourite data.
                    markFavourite(accountID, sessionID, movie.getId(), true);
                }
            } else if (result.url.equals(markFavouriteURL)) {
                if (JsonToPojoParser.getInstance().markFavouriteStatus(result.mResultValue)) {
                    Toast.makeText(MovieDetailActivity.this, "Favourite updated successfully"
                            , Toast.LENGTH_SHORT).show();

                    // change the favourite_image icon
                    favourite_image.setImageResource(R.mipmap.ic_heart_filled);

                }
            }
        } else {
            if (result.mException != null) {
                Toast.makeText(this, result.mException.getLocalizedMessage(), Toast.LENGTH_SHORT)
                        .show();
            }
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
        // NOT IMPLEMENTED
    }

    private void getAccountData() {
        NetworkObject networkObject = new NetworkObject(accountDetailsURL, NetworkObject.HTTP_METHOD.GET);
        networkFragment = NetworkFragment.getInstance(getSupportFragmentManager(), networkObject);
        networkFragment.startDownload();
    }

    private void markFavourite(int accountID, String sessionID, int movieID, boolean isFavourite) {
        // Create the JSON to POST.
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("media_type", "movie");
        jsonObject.addProperty("media_id", String.valueOf(movieID));
        jsonObject.addProperty("favorite", isFavourite ? "true" : "false");

        String postData = jsonObject.toString(); //convert the JSON to string.

        markFavouriteURL = Specs.markFavouriteURL(accountID, sessionID);
        NetworkObject networkObject = new NetworkObject(markFavouriteURL, NetworkObject.HTTP_METHOD.POST, postData);
        networkFragment = NetworkFragment.getInstance(getSupportFragmentManager(), networkObject);
        networkFragment.startDownload();
    }
}
