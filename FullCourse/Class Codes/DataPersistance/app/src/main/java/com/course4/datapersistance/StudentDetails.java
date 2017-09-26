package com.course4.datapersistance;

/**
 * Created by muthuveerappans on 9/16/17.
 */

public class StudentDetails {
    public int regID;
    public String name;
    public int rank;
    public String dob;
    public float percentage;

    public StudentDetails(int regID, String name, int rank, String dob, float percentage) {
        this.regID = regID;
        this.name = name;
        this.rank = rank;
        this.dob = dob;
        this.percentage = percentage;
    }

    @Override
    public String toString() {
        return "" + regID + " | " + name + " | " + "" + rank + " | " + dob + " | " + "" + percentage;
    }
}
