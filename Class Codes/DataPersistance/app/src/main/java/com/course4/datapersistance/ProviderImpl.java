package com.course4.datapersistance;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import static android.provider.BaseColumns._ID;
import static com.course4.datapersistance.DbContract.StudentDetails.COL_REG_ID;
import static com.course4.datapersistance.DbContract.StudentDetails.TABLE_NAME;

/**
 * Created by muthuveerappans on 9/20/17.
 */

public class ProviderImpl extends ContentProvider {
    private static final UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    // creating content URIs
    static {
        uriMatcher.addURI(DbContract.AUTHORITY, "student_details", 1);
        uriMatcher.addURI(DbContract.AUTHORITY, "student_details/#", 2);
    }

    Db db;

    @Override
    public boolean onCreate() {
        db = new Db(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String s1) {
        SQLiteDatabase database = db.getReadableDatabase();

        switch (uriMatcher.match(uri)) {
            case 1:
                return database.query(TABLE_NAME, projection, selection, selectionArgs, null, null, s1);
            case 2:
                // select * from student_details where row_id = 15;
                String selectionNew = COL_REG_ID + " = ? ";
                String[] selectionArgsNew = new String[]{uri.getLastPathSegment()};
                return database.query(TABLE_NAME, projection, selectionNew, selectionArgsNew, null, null, null, s1);
            default:
                throw new IllegalArgumentException("Invalid URI provided. " + uri.toString());
        }
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues contentValues) {
        SQLiteDatabase database = db.getWritableDatabase();

        switch (uriMatcher.match(uri)) {
            case 1:
                long rowID = database.insert(TABLE_NAME, null, contentValues);
                return Uri.withAppendedPath(uri, String.valueOf(rowID));
            case 2:
                throw new IllegalArgumentException("Invalid URI provided. " + uri.toString());
            default:
                throw new IllegalArgumentException("Invalid URI provided. " + uri.toString());
        }
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String s, @Nullable String[] strings) {
        return 0;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues contentValues, @Nullable String s, @Nullable String[] strings) {
        return 0;
    }
}
