package com.example.muthuveerappans.course3.ParserHelpers;

import com.example.muthuveerappans.course3.Modals.Genre;
import com.example.muthuveerappans.course3.Modals.Movie;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by muthuveerappans on 7/25/17.
 */

public class JsonToPojoParser {
    public static JsonToPojoParser jsonToPojoParser;

    // To avoid unnecessary memory allocation, instantiate this object only once.
    public static JsonToPojoParser getInstance() {
        if (jsonToPojoParser == null) {
            jsonToPojoParser = new JsonToPojoParser();
        }
        return jsonToPojoParser;
    }

    // Make the empty constructor private.
    private JsonToPojoParser() {
    }

    public List<Genre> getGenresFromJson(String json) {
        List<Genre> genreList = new ArrayList<>();

        JsonParser parser = new JsonParser();
        JsonObject genresJson = parser.parse(json).getAsJsonObject();

        JsonArray genresJsonArray = genresJson.get("genres").getAsJsonArray();

        for (JsonElement e : genresJsonArray) {
            JsonObject singleGenreJson = e.getAsJsonObject();

            int id = singleGenreJson.get("id").getAsInt();
            String name = singleGenreJson.get("name").getAsString();

            Genre genre = new Genre(id, name);

            genreList.add(genre);
        }

        return genreList;
    }

    public List<Movie> getMoviesFromJson(String json) {
        List<Movie> moviesList = new ArrayList<>();

        JsonParser parser = new JsonParser();
        JsonObject moviesJson = parser.parse(json).getAsJsonObject();

        JsonArray moviesJsonArray = moviesJson.get("results").getAsJsonArray();

        for (JsonElement e : moviesJsonArray) {
            JsonObject movieJson = e.getAsJsonObject();

            int id = movieJson.get("id").getAsInt();
            String title = movieJson.get("title").getAsString();
            String description = movieJson.get("overview").getAsString();
            String language = movieJson.get("original_language").getAsString();
            float voterAvg = movieJson.get("vote_average").getAsFloat();
            String posterPath = movieJson.get("poster_path").getAsString();

            Movie movie = new Movie(id, language, title, description, voterAvg, posterPath);

            moviesList.add(movie);
        }

        return moviesList;
    }
}
