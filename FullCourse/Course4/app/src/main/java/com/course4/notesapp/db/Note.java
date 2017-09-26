package com.course4.notesapp.db;

import android.content.ContentValues;
import android.database.Cursor;

import com.course4.notesapp.List.NoteViewModal;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by muthuveerappans on 8/30/17.
 */

public class Note {
    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());

    public int id;
    public String title;
    public String content;
    public Date timeStamp;

    public Note(int id, String title, String content, Date timeStamp) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.timeStamp = timeStamp;
    }

    public Note(Cursor c) {
        this.id = c.getInt(c.getColumnIndex(NotesContract.NoteEntry._ID));
        this.title = c.getString(c.getColumnIndex(NotesContract.NoteEntry.COLUMN_NAME_TITLE));
        this.content = c.getString(c.getColumnIndex(NotesContract.NoteEntry.COLUMN_NAME_CONTENT));
        try {
            this.timeStamp = dateFormat.parse(c.getString(c.getColumnIndex(NotesContract.NoteEntry.COLUMN_TIMESTAMP)));
        } catch (ParseException e) {
            throw new RuntimeException("Error while parsing the DATATIME from db. " + e.getMessage());
        }
    }

    public ContentValues getAsContentValues() {
        ContentValues contentValues = new ContentValues();
        contentValues.put(NotesContract.NoteEntry.COLUMN_NAME_TITLE, title);
        contentValues.put(NotesContract.NoteEntry.COLUMN_NAME_CONTENT, content);
        contentValues.put(NotesContract.NoteEntry.COLUMN_TIMESTAMP, dateFormat.format(timeStamp));
        return contentValues;
    }

    public static Note adapter(NoteViewModal noteViewModal) {
        return new Note(noteViewModal.id, noteViewModal.title, noteViewModal.content, new Date());
    }
}
