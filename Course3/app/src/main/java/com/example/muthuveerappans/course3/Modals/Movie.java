package com.example.muthuveerappans.course3.Modals;

/**
 * Created by muthuveerappans on 7/25/17.
 */

public class Movie {
    private int id;
    private String language;
    private String title;
    private String description;
    private float voteAverage;
    private String posterPath;

    public Movie(int id, String language, String title, String description, float voteAverage, String posterPath) {
        this.id = id;
        this.language = language;
        this.title = title;
        this.description = description;
        this.voteAverage = voteAverage;
        this.posterPath = posterPath;
    }

    public int getId() {
        return id;
    }

    public String getLanguage() {
        return language;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public float getVoteAverage() {
        return voteAverage;
    }

    public String getPosterPath() {
        return posterPath;
    }
}
