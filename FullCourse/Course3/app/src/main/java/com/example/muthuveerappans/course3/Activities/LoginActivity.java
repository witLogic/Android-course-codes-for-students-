package com.example.muthuveerappans.course3.Activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.muthuveerappans.course3.NetworkHelpers.DownloadCallback;
import com.example.muthuveerappans.course3.NetworkHelpers.NetworkFragment;
import com.example.muthuveerappans.course3.NetworkHelpers.NetworkObject;
import com.example.muthuveerappans.course3.NetworkHelpers.Result;
import com.example.muthuveerappans.course3.ParserHelpers.JsonToPojoParser;
import com.example.muthuveerappans.course3.R;
import com.example.muthuveerappans.course3.Specs;

/**
 * Created by muthuveerappans on 7/26/17.
 */

public class LoginActivity extends AppCompatActivity implements DownloadCallback<Result> {
    private Button loginButton;
    private NetworkFragment networkFragment;

    String authTokenURL;
    String sessionRequestURL;

    SharedPreferences sharedPreferences;

    Boolean isSessionIDSaved = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        sharedPreferences = getSharedPreferences(Specs.prefsFileName, MODE_PRIVATE);

        setContentView(R.layout.login_activity);

        loginButton = (Button) findViewById(R.id.login_button);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createRequestToken();
            }
        });

        authTokenURL = Specs.getAuthTokenURL();

        // Check if the activity is opened from a redirect
        Uri data = this.getIntent().getData();
        if (data != null) {
            String host = data.getHost();
            String path = data.getPath();

            if (host.equals("www.course3_app.com") && path.equals("/login_approved")) {
                // activity is opened through a redirect from the browser
                // call createSessionToken(String) directly
                String requestToken = sharedPreferences.getString(Specs.requestTokenPrefsKey, "");

                if (!requestToken.isEmpty()) {
                    // Disable the login button.
                    loginButton.setEnabled(false);
                    createSessionToken(requestToken);
                }
            }
        }
    }

    @Override
    public void onResponseReceived(Result result) {
        if (loginButton == null) {
            return;
        }

        if (result.mResultValue != null) {
            if (result.url.equals(authTokenURL)) {
                String requestToken = JsonToPojoParser.getInstance().getAuthToken(result.mResultValue);
                // save the data in shared preferences for future use. (persistence storage)
                saveDataInSharedPreferences(Specs.requestTokenPrefsKey, requestToken);

                // redirect the user to browser
                String url = Specs.loginRedirectURL(requestToken);
                Intent viewIntent =
                        new Intent("android.intent.action.VIEW",
                                Uri.parse(url));
                startActivity(viewIntent);

            } else if (result.url.equals(sessionRequestURL)) {
                String sessionID = JsonToPojoParser.getInstance().getSessionID(result.mResultValue);
                // save the data in shared preferences for future use. (persistence storage)
                saveDataInSharedPreferences(Specs.sessionIdPrefsKey, sessionID);

                isSessionIDSaved = true;
            }
        } else {
            if (result.mException != null) {
                Toast.makeText(this, result.mException.getLocalizedMessage(), Toast.LENGTH_SHORT)
                        .show();
            }
        }
    }

    // Saves the data to the shared preferences for future use. Data persistence.
    private void saveDataInSharedPreferences(String key, String value) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key, value);
        editor.apply();
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
        if (networkFragment != null) {
            networkFragment.cancelDownload();
        }

        if (isSessionIDSaved) {
            finish(); // Finish this activity when the sessionID is saved and open the required activity

            // Details of FLAG_ACTIVITY_CLEAR_TOP
            // If set, and the activity being launched is already running in the current task,
            // then instead of launching a new instance of that activity,
            // all of the other activities on top of it will be closed and this
            // Intent will be delivered to the (now on top) old activity as a new Intent.
            // The parent activity should also contain singleTop in manifest.
            //
            // For example, consider a task consisting of the activities: A, B, C, D.
            // If D calls startActivity() with an Intent that resolves to the component of activity B,
            // then C and D will be finished and B receive the given Intent, resulting in the stack now being: A, B.
            // more info: https://developer.android.com/reference/android/content/Intent.html#FLAG_ACTIVITY_CLEAR_TOP
            Intent intent = new Intent(this, MovieDetailActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        }
    }

    private void createRequestToken() {
        NetworkObject networkObject = new NetworkObject(authTokenURL, NetworkObject.HTTP_METHOD.GET);
        networkFragment = NetworkFragment.getInstance(getSupportFragmentManager()
                , networkObject);
        networkFragment.startDownload();
    }

    private void createSessionToken(String requestToken) {
        sessionRequestURL = Specs.getSessionURL(requestToken);
        NetworkObject networkObject = new NetworkObject(sessionRequestURL, NetworkObject.HTTP_METHOD.GET);
        networkFragment = NetworkFragment.getInstance(getSupportFragmentManager()
                , networkObject);
        networkFragment.startDownload();
    }

}
