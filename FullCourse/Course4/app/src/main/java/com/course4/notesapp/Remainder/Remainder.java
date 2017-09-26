package com.course4.notesapp.Remainder;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by muthuveerappans on 9/15/17.
 */

public class Remainder implements Parcelable {
    String notes;
    int hour;
    int min;

    public Remainder(String notes, int hour, int min) {
        this.notes = notes;
        this.hour = hour;
        this.min = min;
    }

    protected Remainder(Parcel in) {
        notes = in.readString();
        hour = in.readInt();
        min = in.readInt();
    }

    public static final Creator<Remainder> CREATOR = new Creator<Remainder>() {
        @Override
        public Remainder createFromParcel(Parcel in) {
            return new Remainder(in);
        }

        @Override
        public Remainder[] newArray(int size) {
            return new Remainder[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(notes);
        parcel.writeInt(hour);
        parcel.writeInt(min);
    }
}
