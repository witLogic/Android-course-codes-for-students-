package com.course4.notesapp.db;

import android.provider.BaseColumns;

/**
 * Created by muthuveerappans on 8/30/17.
 */

public final class NotesContract {
    private NotesContract() {
    }

    public static class NoteEntry implements BaseColumns {
        public static final String TABLE = "notes";
        public static final String COLUMN_NAME_TITLE = "title";
        public static final String COLUMN_NAME_CONTENT = "content";
        public static final String COLUMN_TIMESTAMP = "timestamp";
    }
}
