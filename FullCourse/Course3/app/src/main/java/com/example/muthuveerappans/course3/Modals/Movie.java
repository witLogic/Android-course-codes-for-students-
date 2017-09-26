package com.example.muthuveerappans.course3.Modals;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by muthuveerappans on 7/25/17.
 */

public class Movie implements Parcelable {
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

    protected Movie(Parcel in) {
        id = in.readInt();
        language = in.readString();
        title = in.readString();
        description = in.readString();
        voteAverage = in.readFloat();
        posterPath = in.readString();
    }

    public static final Creator<Movie> CREATOR = new Creator<Movie>() {
        @Override
        public Movie createFromParcel(Parcel in) {
            return new Movie(in);
        }

        @Override
        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(language);
        dest.writeString(title);
        dest.writeString(description);
        dest.writeFloat(voteAverage);
        dest.writeString(posterPath);
    }
}
