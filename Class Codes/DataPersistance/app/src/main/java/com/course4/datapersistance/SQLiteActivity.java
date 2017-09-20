package com.course4.datapersistance;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;

import static com.course4.datapersistance.DbContract.StudentDetails.COL_DOB;
import static com.course4.datapersistance.DbContract.StudentDetails.COL_NAME;
import static com.course4.datapersistance.DbContract.StudentDetails.COL_PERCENTAGE;
import static com.course4.datapersistance.DbContract.StudentDetails.COL_RANK;
import static com.course4.datapersistance.DbContract.StudentDetails.COL_REG_ID;
import static com.course4.datapersistance.DbContract.StudentDetails.TABLE_NAME;

/**
 * Created by muthuveerappans on 9/16/17.
 */

public class SQLiteActivity extends AppCompatActivity {
    EditText regID_edt, name_edt, rank_edt, dob_edt, percentage_edt;
    Db db;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.sqlite_activity);

        regID_edt = (EditText) findViewById(R.id.editText_regid);
        name_edt = (EditText) findViewById(R.id.editText_name);
        rank_edt = (EditText) findViewById(R.id.editText_rank);
        dob_edt = (EditText) findViewById(R.id.editText_dob);
        percentage_edt = (EditText) findViewById(R.id.editText_percent);

        db = new Db(this);

        findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int regID = Integer.valueOf(regID_edt.getText().toString());
                String name = name_edt.getText().toString();
                int rank = Integer.valueOf(rank_edt.getText().toString());
                String dob = dob_edt.getText().toString();
                float percent = Float.valueOf(percentage_edt.getText().toString());

                StudentDetails studentDetails = new StudentDetails(regID, name, rank, dob, percent);
                insertData(studentDetails);
            }
        });

        TextView queryTxt = (TextView) findViewById(R.id.query_text);
        //ArrayList<StudentDetails> result = queryAllData();
        ArrayList<StudentDetails> result = queryAllDataContentProvider();

        StringBuilder sb = new StringBuilder();
        sb.append("Query: select * from student_details \n");

        for (StudentDetails sd : result) {
            sb.append(sd.toString());
            sb.append("\n");
        }

//        sb.append("Query: select * from student_details where rank = 4 p percentage = 70 \n");
//
//        ArrayList<StudentDetails> r = query(4, 70);
//        sb.append(r.get(0).toString());

        queryTxt.setText(sb.toString());
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

    public void insertData(StudentDetails studentDetails) {
        SQLiteDatabase sqLiteDatabase = db.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_REG_ID, studentDetails.regID);
        contentValues.put(COL_NAME, studentDetails.name);
        contentValues.put(COL_RANK, studentDetails.rank);
        contentValues.put(COL_DOB, studentDetails.dob);
        contentValues.put(COL_PERCENTAGE, studentDetails.percentage);

        long rowID = sqLiteDatabase.insert(TABLE_NAME, null, contentValues);

        Log.i("SQLiteActivity", "ID of the inserted row: " + rowID);

//        String insert_sql = "insert into" + TABLE_NAME + "(" + COL_REG_ID +
//                ", " + COL_NAME + ", " + COL_RANK + ", " + COL_DOB + ", " + COL_PERCENTAGE + ") " +
//                "values (" + studentDetails.regID + ", " + studentDetails.name + "," +
//                " " + studentDetails.rank + ", " + studentDetails.dob + ", " + studentDetails.percentage + ")";
//
//        sqLiteDatabase.execSQL(insert_sql);
    }

    public ArrayList<StudentDetails> queryAllData() {
        ArrayList<StudentDetails> result = new ArrayList<>();

        SQLiteDatabase sqLiteDatabase = db.getReadableDatabase();
        // String[] cols = new String[] {COL_NAME, COL_DOB};
        Cursor cursor = sqLiteDatabase.query(TABLE_NAME, null, null, null, null, null, null);
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

    public ArrayList<StudentDetails> query(int rank, float percentage) {
        ArrayList<StudentDetails> result = new ArrayList<>();

        SQLiteDatabase sqLiteDatabase = db.getReadableDatabase();

        String selection = "rank = ? and percentage = ?";
        String[] args = new String[]{String.valueOf(rank), String.valueOf(percentage)};
        Cursor cursor = sqLiteDatabase.query(TABLE_NAME, null, selection, args, null, null, null);

        if (cursor.moveToFirst()) {
            do {
                int regID = cursor.getInt(cursor.getColumnIndex(COL_REG_ID));
                String name = cursor.getString(cursor.getColumnIndex(COL_NAME));
                String dob = cursor.getString(cursor.getColumnIndex(COL_DOB));
                float p = cursor.getFloat(cursor.getColumnIndex(COL_PERCENTAGE));
                int r = cursor.getInt(cursor.getColumnIndex(COL_RANK));

                StudentDetails s = new StudentDetails(regID, name, r, dob, p);
                result.add(s);

            } while (cursor.moveToNext());
        }

        cursor.close();

        return result;
    }

}
