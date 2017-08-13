package com.example.muthuveerappans.myapplication;

import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.LogPrinter;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i("", "onCreate");

    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.i("","onstart");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i("","onresume");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.i("","onpause");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.i("","onstop");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.i("","ondestroy");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.i("","onrestart");
    }
}
