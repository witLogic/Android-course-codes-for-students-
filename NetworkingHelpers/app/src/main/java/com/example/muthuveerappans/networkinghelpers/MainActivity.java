package com.example.muthuveerappans.networkinghelpers;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.muthuveerappans.networkinghelpers.Internet.DownloadCallback;
import com.example.muthuveerappans.networkinghelpers.Internet.NetworkObject;
import com.example.muthuveerappans.networkinghelpers.Internet.NetworkTask;
import com.example.muthuveerappans.networkinghelpers.Internet.Result;
import com.google.gson.JsonObject;

public class MainActivity extends AppCompatActivity implements DownloadCallback<Result> {
    TextView textView;
    NetworkTask networkTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textView = (TextView) findViewById(R.id.text_view);

        // URL's got from https://jsonplaceholder.typicode.com/
        String URL = "https://jsonplaceholder.typicode.com/posts";

        // URL that gets GENRES from the TMDB server.
        NetworkObject networkObject = new NetworkObject(URL, NetworkObject.HTTP_METHOD.GET);

        // POST sample request
        // Construct a json post body using GSON library
//        JsonObject bodyJSON = new JsonObject();
//        bodyJSON.addProperty("userId", "1");
//        bodyJSON.addProperty("title", "Test from app");
//        bodyJSON.addProperty("body", "This is a test POST request from the app.");
//        String body = bodyJSON.toString();
//
//        NetworkObject networkObject = new NetworkObject(URL, NetworkObject.HTTP_METHOD.POST, body);

        networkTask = new NetworkTask(networkObject, this);

        findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textView.setText("");
                networkTask.startDownload();
            }
        });
    }

    @Override
    public void onResponseReceived(Result result) {
        if (!isActivityDestroyed()) {
            textView.setText(result.mResultValue);
        }
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
        if (networkTask != null) {
            networkTask.cancelDownload();
        }

        if (!isActivityDestroyed()) {
            Toast.makeText(this, "Finished network task", Toast.LENGTH_SHORT).show();
        }
    }

    private boolean isActivityDestroyed() {
        return textView == null;
    }
}
