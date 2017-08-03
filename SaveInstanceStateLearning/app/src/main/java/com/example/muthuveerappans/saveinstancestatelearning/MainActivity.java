package com.example.muthuveerappans.saveinstancestatelearning;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import java.util.Random;

public class MainActivity extends AppCompatActivity {
    int num1;
    int num2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TextView textView = (TextView) findViewById(R.id.text_view);

        if (savedInstanceState == null) {
            num1 = new Random().nextInt(10);
            num2 = new Random().nextInt(10);
        } else {
            num1 = savedInstanceState.getInt("num1");
            num2 = savedInstanceState.getInt("num2");
        }

        textView.setText("" + num1 + " + " + num2 + " = " + (num1 + num2));
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putInt("num1", num1);
        outState.putInt("num2", num2);
    }
}
