package com.course4.intentactionsandotherattributes;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String url = "https://www.google.co.in/?gws_rd=ssl";
                Intent viewIntent =
                        new Intent("android.intent.action.VIEW",
                                Uri.parse(url));
                startActivity(viewIntent);
            }
        });
    }
}
