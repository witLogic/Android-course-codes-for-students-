package com.course4.datapersistance;

import android.provider.BaseColumns;

/**
 * Created by muthuveerappans on 9/16/17.
 */

public class DbContract {
    public static String AUTHORITY = "com.course4.datapersistance.provider";
    public static String DB_NAME = "details";
    public static int DB_VERSION = 1;

    public static class StudentDetails implements BaseColumns {
        public static String TABLE_NAME = "student_details";
        public static String COL_REG_ID = "reg_id";
        public static String COL_NAME = "name";
        public static String COL_RANK = "rank";
        public static String COL_DOB = "dob";
        public static String COL_PERCENTAGE = "percentage";
    }
}
