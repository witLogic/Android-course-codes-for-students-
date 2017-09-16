package com.course4.datapersistance;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.Random;

public class SharedPrefsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TextView t = (TextView) findViewById(R.id.text);

        Random random = new Random();

        SharedPreferences sharedPreferences = getSharedPreferences("com.course4.datapersistance.prefs", MODE_PRIVATE);
        int dataFromPrefs = sharedPreferences.getInt("data", -1);

        if (dataFromPrefs == -1) {
            dataFromPrefs = random.nextInt(9);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putInt("data", dataFromPrefs);
            editor.putString("data_test", "Hello world");
            editor.apply();
        }

        t.setText("" + dataFromPrefs);
    }
}
