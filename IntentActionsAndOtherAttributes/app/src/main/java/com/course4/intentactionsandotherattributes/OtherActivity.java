package com.course4.intentactionsandotherattributes;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

/**
 * Created by muthuveerappans on 9/5/17.
 */

public class OtherActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.other_activity);

        Uri data = this.getIntent().getData();
        String host = data.getHost();
        String path = data.getPath();

        TextView t = (TextView) findViewById(R.id.textView);
        t.setText("Data from the URL: " + host + path);
    }
}
