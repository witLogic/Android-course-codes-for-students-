package com.example.muthuveerappans.course3;

import android.net.Uri;

/**
 * Created by muthuveerappans on 7/25/17.
 */

public class Specs {
    // TMDB API Key
    public static final String TMDB_API_KEY = "746141c238b6e0d721a4e96d364fec68";

    // http specs
    public static final String scheme = "https";
    public static final String domain = "api.themoviedb.org";
    public static final String basePath = "3";

    // API method for get Genre list.
    public static String genreListURL() {
        Uri.Builder builder = baseURI();
        builder.appendPath("genre")
                .appendPath("movie")
                .appendPath("list")
                .appendQueryParameter("api_key", TMDB_API_KEY);

        return builder.build().toString();
    }

    // API method for get Movie list for a particular GenreID.
    public static String genreMovieListForGenreURL(int genreID) {
        Uri.Builder builder = baseURI();
        builder.appendPath("genre")
                .appendPath(String.valueOf(genreID))
                .appendPath("movies")
                .appendQueryParameter("api_key", TMDB_API_KEY);

        return builder.build().toString();
    }

    // Generic method for all API methods.
    private static Uri.Builder baseURI() {
        Uri.Builder builder = new Uri.Builder();
        builder.scheme(scheme)
                .authority(domain)
                .appendPath(basePath)
                .appendQueryParameter("language", "en-US");
        return builder;
    }

    // construct movie image path URL
    public static String getMoviePosterURL(String path) {
        Uri.Builder builder = new Uri.Builder();
        builder.scheme("https")
                .authority("image.tmdb.org")
                .appendPath("t")
                .appendPath("p")
                .appendPath("w500")
                .appendPath(path.substring(1));

        return builder.build().toString();
    }
}
