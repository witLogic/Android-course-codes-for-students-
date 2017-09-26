package com.example.muthuveerappans.course3;

import android.net.Uri;

/**
 * Created by muthuveerappans on 7/25/17.
 */

public class Specs {
    // TMDB API Key
    public static final String TMDB_API_KEY = "746141c238b6e0d721a4e96d364fec68";

    // shared prefs
    public static final String prefsFileName = "com.example.muthuveerappans.course3.prefs";
    public static final String requestTokenPrefsKey = "auth_token";
    public static final String sessionIdPrefsKey = "session_id";

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

    // authentication token
    public static String getAuthTokenURL() {
        Uri.Builder builder = baseURI();
        builder.appendPath("authentication")
                .appendPath("token")
                .appendPath("new")
                .appendQueryParameter("api_key", TMDB_API_KEY);

        return builder.build().toString();
    }

    // session token
    public static String getSessionURL(String requestToken) {
        Uri.Builder builder = baseURI();
        builder.appendPath("authentication")
                .appendPath("session")
                .appendPath("new")
                .appendQueryParameter("api_key", TMDB_API_KEY)
                .appendQueryParameter("request_token", requestToken);

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

    public static String loginRedirectURL(String requestToken) {
        Uri.Builder builder = new Uri.Builder();
        builder.scheme("https")
                .authority("www.themoviedb.org")
                .appendPath("authenticate")
                .appendPath(requestToken)
                .appendQueryParameter("redirect_to", "http://www.course3_app.com/login_approved"); // name that you gave in android manifest inside data tag

        return builder.build().toString();
    }

    public static String accountDetailsURL(String sessionID) {
        Uri.Builder builder = baseURI();
        builder.appendPath("account")
                .appendQueryParameter("api_key", TMDB_API_KEY)
                .appendQueryParameter("session_id", sessionID);

        return builder.build().toString();
    }

    public static String markFavouriteURL(int accountID, String sessionID) {
        Uri.Builder builder = baseURI();
        builder.appendPath("account")
                .appendPath(String.valueOf(accountID))
                .appendPath("favorite")
                .appendQueryParameter("api_key", TMDB_API_KEY)
                .appendQueryParameter("session_id", sessionID);

        return builder.build().toString();
    }
}
