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

public class MainActivity extends AppCompatActivity implements DownloadCallback<Result> {
    TextView textView;
    NetworkTask networkTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textView = (TextView) findViewById(R.id.text_view);

        // URL's got from https://jsonplaceholder.typicode.com/
        // URL that gets GENRES from the TMDB server.
        String URL = "https://jsonplaceholder.typicode.com/posts";
        NetworkObject networkObject = new NetworkObject(URL, NetworkObject.HTTP_METHOD.GET);

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
