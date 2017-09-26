package com.course4.notesapp.db;

import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by muthuveerappans on 8/30/17.
 */

public final class NotesContract {
    public static final String CONTENT_AUTHORITY = "com.course4.notesapp.provider";
    public static final String BASE_CONTENT_URI = "content://" + CONTENT_AUTHORITY;
    public static final String PATH_NOTES = "notes";
    public static final String PATH_NOTES_SINGLE_ROW = "notes/#";
    public static final String NOTES_TABLE_CONTENT_URI = BASE_CONTENT_URI + "/" + PATH_NOTES;

    private NotesContract() {
    }

    public static Uri NOTES_URI() {
        return Uri.parse(NOTES_TABLE_CONTENT_URI);
    }

    public static Uri NOTES_SINGLE_ROW_URI(int _id) {
        return ContentUris.withAppendedId(NOTES_URI(), _id);
    }

    public static class NoteEntry implements BaseColumns {
        public static final String TABLE = "notes";
        public static final String COLUMN_NAME_TITLE = "title";
        public static final String COLUMN_NAME_CONTENT = "content";
        public static final String COLUMN_TIMESTAMP = "timestamp";
    }
}
