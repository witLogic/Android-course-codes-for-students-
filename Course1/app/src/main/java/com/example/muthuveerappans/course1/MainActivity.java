package com.example.muthuveerappans.course1;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by muthuveerappans on 7/14/17.
 */

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Set the activity_main.xml file as the view for this entire page.
        setContentView(R.layout.activity_main);
    }
}
