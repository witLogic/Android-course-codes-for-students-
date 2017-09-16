package com.course4.datapersistance;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

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
}
