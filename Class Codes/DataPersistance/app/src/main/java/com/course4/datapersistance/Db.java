package com.course4.datapersistance;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import static com.course4.datapersistance.DbContract.StudentDetails.COL_DOB;
import static com.course4.datapersistance.DbContract.StudentDetails.COL_NAME;
import static com.course4.datapersistance.DbContract.StudentDetails.COL_PERCENTAGE;
import static com.course4.datapersistance.DbContract.StudentDetails.COL_RANK;
import static com.course4.datapersistance.DbContract.StudentDetails.COL_REG_ID;
import static com.course4.datapersistance.DbContract.StudentDetails.TABLE_NAME;

/**
 * Created by muthuveerappans on 9/16/17.
 */

public class Db extends SQLiteOpenHelper {

    public Db(Context context) {
        super(context, DbContract.DB_NAME, null, DbContract.DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + "(" +
                COL_REG_ID + " INTEGER," +
                COL_NAME + " TEXT NOT NULL," +
                COL_RANK + " INTEGER NOT NULL," +
                COL_DOB + " TEXT NOT NULL," +
                COL_PERCENTAGE + " REAL NOT NULL," +
                "PRIMARY KEY(" + COL_REG_ID + ")" +
                ");";
        sqLiteDatabase.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
    }
}
