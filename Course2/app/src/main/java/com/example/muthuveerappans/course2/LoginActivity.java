package com.example.muthuveerappans.course2;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    private EditText username_editText;
    private EditText password_editText;
    private Button login_button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);

        // Initialize the view objects.
        username_editText = (EditText) findViewById(R.id.username_editText);
        password_editText = (EditText) findViewById(R.id.password_editText);
        login_button = (Button) findViewById(R.id.login_button);
        login_button.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.login_button) {
            if (checkLoginStatus()) {
                Toast.makeText(LoginActivity.this, "Login successful", Toast.LENGTH_SHORT).show();

                // Open the Puzzle activity.
                openPuzzleActivity();
            }
        }
    }

    private boolean checkLoginStatus() {
        return Data.checkLoginData(getUsername(), getPassword());
    }

    private String getUsername() {
        return username_editText.getText().toString();
    }

    private String getPassword() {
        return password_editText.getText().toString();
    }

    private void openPuzzleActivity() {
        Intent intent = new Intent(LoginActivity.this, PuzzleActivity.class);
        startActivity(intent);
    }
}
