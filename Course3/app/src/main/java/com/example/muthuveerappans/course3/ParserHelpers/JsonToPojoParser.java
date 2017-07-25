package com.example.muthuveerappans.course3.ParserHelpers;

import com.example.muthuveerappans.course3.Modals.Genre;
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
}
