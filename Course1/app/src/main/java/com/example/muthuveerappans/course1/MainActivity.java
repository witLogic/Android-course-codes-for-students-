package com.example.muthuveerappans.course1;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by muthuveerappans on 7/14/17.
 */

/*
Try and see:
+ Create a simple portfolio page of yours with the following content and attributes:
+ Place your name in the top left of the page.
+ Make your name bold.
+ Place your contact number below your name
+ Email address below the contact number.
+ Place your passport size photo on the top right corner of the page.
+ Enter few lines about yourself.
+ The background color of the page should be #ededed.
+ All text should be in black.
 */

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Set the activity_main.xml file as the view for this entire page.
        setContentView(R.layout.activity_main);
    }
}
