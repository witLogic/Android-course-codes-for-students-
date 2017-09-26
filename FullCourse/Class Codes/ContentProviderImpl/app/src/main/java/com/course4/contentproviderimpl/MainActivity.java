package com.course4.contentproviderimpl;

import android.database.Cursor;
import android.net.Uri;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import java.util.ArrayList;

import static com.course4.contentproviderimpl.DbContract.StudentDetails.COL_DOB;
import static com.course4.contentproviderimpl.DbContract.StudentDetails.COL_NAME;
import static com.course4.contentproviderimpl.DbContract.StudentDetails.COL_PERCENTAGE;
import static com.course4.contentproviderimpl.DbContract.StudentDetails.COL_RANK;
import static com.course4.contentproviderimpl.DbContract.StudentDetails.COL_REG_ID;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ArrayList<StudentDetails> result = queryAllDataContentProvider();

        for (StudentDetails sd : result) {
            Log.i("content_impl", sd.toString());
        }
    }

    public ArrayList<StudentDetails> queryAllDataContentProvider() {
        ArrayList<StudentDetails> result = new ArrayList<>();
        Cursor cursor = getContentResolver().query(Uri.parse("content://com.course4.datapersistance.provider/student_details")
                , null, null, null, null, null);

        if (cursor.moveToFirst()) {
            do {
                int regID = cursor.getInt(cursor.getColumnIndex(COL_REG_ID));
                String name = cursor.getString(cursor.getColumnIndex(COL_NAME));
                String dob = cursor.getString(cursor.getColumnIndex(COL_DOB));
                float percentage = cursor.getFloat(cursor.getColumnIndex(COL_PERCENTAGE));
                int rank = cursor.getInt(cursor.getColumnIndex(COL_RANK));

                StudentDetails s = new StudentDetails(regID, name, rank, dob, percentage);
                result.add(s);

            } while (cursor.moveToNext());
        }

        cursor.close();

        return result;
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return null;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }
}
