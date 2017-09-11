package com.course4.notesapp.List;

import android.os.Parcel;
import android.os.Parcelable;

import com.course4.notesapp.db.Note;

/**
 * Created by muthuveerappans on 8/30/17.
 */

public class NoteViewModal implements Parcelable {
    public int id;
    public String title;
    public String content;

    public NoteViewModal(int id, String title, String content) {
        this.id = id;
        this.title = title;
        this.content = content;
    }

    protected NoteViewModal(Parcel in) {
        id = in.readInt();
        title = in.readString();
        content = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(title);
        dest.writeString(content);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<NoteViewModal> CREATOR = new Creator<NoteViewModal>() {
        @Override
        public NoteViewModal createFromParcel(Parcel in) {
            return new NoteViewModal(in);
        }

        @Override
        public NoteViewModal[] newArray(int size) {
            return new NoteViewModal[size];
        }
    };

    public static NoteViewModal adapter(Note note) {
        return new NoteViewModal(note.id, note.title, note.content);
    }
}
