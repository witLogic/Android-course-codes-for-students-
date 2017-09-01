package com.course4.notesapp.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by muthuveerappans on 8/30/17.
 */

public class DbCreator extends SQLiteOpenHelper {
    // If you change the database schema, you must increment the database version.
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "Notes.db";

    public String CREATE_NOTES_TABLE = "CREATE TABLE " + NotesContract.NoteEntry.TABLE + " (" +
            NotesContract.NoteEntry._ID + " INTEGER PRIMARY KEY," +
            NotesContract.NoteEntry.COLUMN_NAME_TITLE + " TEXT," +
            NotesContract.NoteEntry.COLUMN_NAME_CONTENT + " TEXT," +
            NotesContract.NoteEntry.COLUMN_TIMESTAMP + " DATETIME" + ")";

    private static final String DELETE_NOTES_TABLE =
            "DROP TABLE IF EXISTS " + NotesContract.NoteEntry.TABLE;

    public DbCreator(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(CREATE_NOTES_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        // This database is only for learning purposes, so its upgrade policy is
        // to simply to discard the data and start over
        sqLiteDatabase.execSQL(DELETE_NOTES_TABLE);
        onCreate(sqLiteDatabase);
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        super.onDowngrade(db, oldVersion, newVersion);
    }
}
