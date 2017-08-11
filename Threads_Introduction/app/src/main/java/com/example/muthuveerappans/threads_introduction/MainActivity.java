package com.example.muthuveerappans.threads_introduction;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    TextView resultTxt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Log.i("test", "Main Thread :" + Thread.currentThread().getId());

        final EditText editText = (EditText) findViewById(R.id.editText);
        resultTxt = (TextView) findViewById(R.id.result_text);

        findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int number = Integer.valueOf(editText.getText().toString());
                new LongOperation().execute(number);
            }
        });


        // static, global discussion
        Test t1 = new Test();
        Test t2 = new Test();

        t1.incrA();
        t2.incrA();

        t1.print();
        t2.print();
    }

    private class LongOperation extends AsyncTask<Integer, Void, Integer> {

        public int fibonacci(int n) {
            if (n == 0)
                return 0;
            else if (n == 1)
                return 1;
            else
                return fibonacci(n - 1) + fibonacci(n - 2);
        }

        @Override
        protected Integer doInBackground(Integer... params) {
            Log.i("test", "Background Thread :" + Thread.currentThread().getId());
            int number = params[0];
            int result = fibonacci(number);
            return result;
        }

        @Override
        protected void onPostExecute(Integer integer) {
            Log.i("test", "Main Thread :" + Thread.currentThread().getId());
            resultTxt.setText("" + integer);
        }
    }

    // static, global discussion
    public static class Test {
        int a = 0;
        static int a1 = 0;

        public void incrA() {
            a += 1;
            a1 += 1;
        }

        public void print() {
            Log.i("t", "a: " + a);
            Log.i("t", "a1: " + a1);
        }
    }
}
