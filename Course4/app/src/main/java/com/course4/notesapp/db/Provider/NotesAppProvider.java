package com.course4.notesapp.db.Provider;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.course4.notesapp.db.DbCreator;

import static android.provider.BaseColumns._ID;
import static com.course4.notesapp.db.NotesContract.CONTENT_AUTHORITY;
import static com.course4.notesapp.db.NotesContract.NOTES_TABLE_CONTENT_URI;
import static com.course4.notesapp.db.NotesContract.NoteEntry.TABLE;
import static com.course4.notesapp.db.NotesContract.PATH_NOTES;
import static com.course4.notesapp.db.NotesContract.PATH_NOTES_SINGLE_ROW;

/**
 * Created by muthuveerappans on 9/11/17.
 */

public class NotesAppProvider extends ContentProvider {
    private static final UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    static {
        uriMatcher.addURI(CONTENT_AUTHORITY, PATH_NOTES, 1);
        uriMatcher.addURI(CONTENT_AUTHORITY, PATH_NOTES_SINGLE_ROW, 2);
    }

    DbCreator dbCreator;

    @Override
    public boolean onCreate() {
        dbCreator = new DbCreator(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] strings, @Nullable String s, @Nullable String[] strings1, @Nullable String sortOrder) {
        String selection = null;
        String selectionArgs[] = null;

        switch (uriMatcher.match(uri)) {
            case 1:
                break;
            case 2:
                selection = _ID + " = ? ";
                selectionArgs = new String[]{uri.getLastPathSegment()};
                break;
            default:
                throw new IllegalArgumentException("Invalid URI provided. " + uri.toString());
        }
        SQLiteDatabase db = dbCreator.getReadableDatabase();
        return db.query(TABLE, strings, selection, selectionArgs, null, null, sortOrder);
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        String dataType = null;
        switch (uriMatcher.match(uri)) {
            case 1:
                dataType = "vnd.android.cursor.dir/vnd" + "." + CONTENT_AUTHORITY + "." + PATH_NOTES;
                break;
            case 2:
                dataType = "vnd.android.cursor.item/vnd" + "." + CONTENT_AUTHORITY + "." + PATH_NOTES;
                break;
        }
        return dataType;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues contentValues) {
        SQLiteDatabase db = dbCreator.getWritableDatabase();
        switch (uriMatcher.match(uri)) {
            case 1:
                long rowID = db.insert(TABLE, null, contentValues);
                return Uri.withAppendedPath(uri, String.valueOf(rowID));
            default:
                throw new IllegalArgumentException("Invalid URI provided. " + uri.toString());
        }
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String s, @Nullable String[] strings) {
        String selection = null;
        String selectionArgs[] = null;

        switch (uriMatcher.match(uri)) {
            case 2:
                selection = _ID + " = ? ";
                selectionArgs = new String[]{uri.getLastPathSegment()};
                break;
            default:
                throw new IllegalArgumentException("Invalid URI provided. " + uri.toString());
        }

        SQLiteDatabase db = dbCreator.getWritableDatabase();
        return db.delete(TABLE, selection, selectionArgs);
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues contentValues, @Nullable String s, @Nullable String[] strings) {
        String selection = null;
        String selectionArgs[] = null;

        switch (uriMatcher.match(uri)) {
            case 2:
                selection = _ID + " = ? ";
                selectionArgs = new String[]{uri.getLastPathSegment()};
                break;
            default:
                throw new IllegalArgumentException("Invalid URI provided. " + uri.toString());
        }

        SQLiteDatabase db = dbCreator.getWritableDatabase();
        return db.update(TABLE, contentValues, selection, selectionArgs);
    }
}
