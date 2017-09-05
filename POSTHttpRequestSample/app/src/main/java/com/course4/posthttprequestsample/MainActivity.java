package com.course4.posthttprequestsample;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.course4.posthttprequestsample.Internet.DownloadCallback;
import com.course4.posthttprequestsample.Internet.NetworkObject;
import com.course4.posthttprequestsample.Internet.NetworkTask;
import com.course4.posthttprequestsample.Internet.Result;
import com.google.gson.JsonObject;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final TextView textView = (TextView) findViewById(R.id.textView);

        findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                JsonObject jsonObject = new JsonObject();
                jsonObject.addProperty("userId", 1);
                jsonObject.addProperty("id", 1);
                jsonObject.addProperty("title", "sunt aut facere repellat provident occaecati excepturi optio reprehenderit");
                jsonObject.addProperty("body", "quia et suscipit\\nsuscipit recusandae consequuntur expedita et cum\\nreprehenderit molestiae ut ut quas totam\\nnostrum rerum est autem sunt rem eveniet architecto");

                new NetworkTask(new NetworkObject("https://jsonplaceholder.typicode.com/posts"
                        , NetworkObject.HTTP_METHOD.POST, jsonObject.toString())
                        , new DownloadCallback<Result>() {
                    @Override
                    public void onResponseReceived(Result result) {
                        textView.setText(result.mResultValue);
                    }

                    @Override
                    public NetworkInfo getActiveNetworkInfo() {
                        ConnectivityManager connectivityManager =
                                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
                        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
                        return networkInfo;
                    }

                    @Override
                    public void onProgressUpdate(int progressCode, int percentComplete) {

                    }

                    @Override
                    public void onFinish() {

                    }
                }).startDownload();
            }
        });
    }
}
