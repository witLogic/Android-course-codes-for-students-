package com.course4.notesapp.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

import java.util.ArrayList;

/**
 * Created by muthuveerappans on 8/30/17.
 */

public class NotesCRUDHelper {
    public static NotesCRUDHelper notesCRUDHelper;
    private DbCreator dbCreator;

    // singleton pattern
    public static NotesCRUDHelper getInstance(Context context) {
        if (notesCRUDHelper == null) {
            notesCRUDHelper = new NotesCRUDHelper(context);
        }
        return notesCRUDHelper;
    }

    private NotesCRUDHelper(Context context) {
        dbCreator = new DbCreator(context);
    }

    public int delete(Note note) {
        SQLiteDatabase db = dbCreator.getWritableDatabase();

        String selection = NotesContract.NoteEntry._ID + " = ? ";
        String selectionArgs[] = {String.valueOf(note.id)};

        return db.delete(NotesContract.NoteEntry.TABLE, selection, selectionArgs);
    }

    public int update(Note note) {
        SQLiteDatabase db = dbCreator.getWritableDatabase();

        String selection = NotesContract.NoteEntry._ID + " = ? ";
        String selectionArgs[] = {String.valueOf(note.id)};

        return db.update(NotesContract.NoteEntry.TABLE, note.getAsContentValues(), selection, selectionArgs);
    }

    public long insert(Note note) {
        SQLiteDatabase db = dbCreator.getWritableDatabase();
        /*
        2'nd parameter :  SQL doesn't allow inserting a completely empty row without naming at least one column name.
        If your provided values is empty, no column names are known and an empty row can't be inserted.
        If not set to null, the nullColumnHack parameter provides the name of nullable
        column name to explicitly insert a NULL into in the case where your values is empty.
         */
        return db.insert(NotesContract.NoteEntry.TABLE, null, note.getAsContentValues());
    }

    public ArrayList<Note> query(int id, boolean getAll) {
        String projection[] = {
                NotesContract.NoteEntry._ID,
                NotesContract.NoteEntry.COLUMN_NAME_TITLE,
                NotesContract.NoteEntry.COLUMN_NAME_CONTENT,
                NotesContract.NoteEntry.COLUMN_TIMESTAMP
        };

        /* USE of '?':
        If you do this, you're allowing the user to concatenate malicious SQL onto your SQL statement.
        For example, the user could enter "nothing; DROP TABLE *;" for mUserInput,
        which would result in the selection clause var = nothing; DROP TABLE *;.
        Since the selection clause is treated as an SQL statement,
        this might cause the provider to erase all of the tables in the underlying SQLite database
        (unless the provider is set up to catch SQL injection attempts).
        To avoid this problem, use a selection clause that uses ? as a replaceable parameter
        and a separate array of selection arguments. When you do this, the user input is bound directly to the query
        rather than being interpreted as part of an SQL statement. Because it's not treated as SQL,
        the user input can't inject malicious SQL.
        */

        String selection = null;
        String selectionArgs[] = null;

        if (!getAll) {
            selection = NotesContract.NoteEntry._ID + " = ? ";

            selectionArgs = new String[1];
            selectionArgs[0] = String.valueOf(id);
        }

        /*The Android system calls onCreate() when it starts up the provider.
        You should perform only fast-running initialization tasks in this method,
        and defer database creation and data loading until the provider actually receives a request for the data.
        If you do lengthy tasks in onCreate(), you will slow down your provider's startup.
        In turn, this will slow down the response from the provider to other applications.

        For example, if you are using an SQLite database you can create a new SQLiteOpenHelper object in
        ContentProvider.onCreate(), and then create the SQL tables the first time you open the database.
        To facilitate this, the first time you call getWritableDatabase(),
        it automatically calls the SQLiteOpenHelper.onCreate() method.*/

        SQLiteDatabase db = dbCreator.getReadableDatabase();

        Cursor cursor = db.query(NotesContract.NoteEntry.TABLE, projection, selection, selectionArgs, null, null, null);

        ArrayList<Note> noteArrayList = new ArrayList<>();
        if (cursor.moveToFirst()) {
            do {
                noteArrayList.add(new Note(cursor));
            } while (cursor.moveToNext());
        }
        cursor.close();

        return noteArrayList;
    }

    public void closeDb() {
        dbCreator.close();
    }
}
