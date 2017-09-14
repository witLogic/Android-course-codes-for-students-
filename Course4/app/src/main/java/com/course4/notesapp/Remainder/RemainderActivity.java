package com.course4.notesapp.Remainder;

import android.app.AlarmManager;
import android.app.Dialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.NavUtils;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.support.v7.app.AppCompatActivity;
import android.text.format.DateFormat;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TimePicker;

import com.course4.notesapp.Constants;
import com.course4.notesapp.List.NotesListActivity;
import com.course4.notesapp.R;

import java.util.Calendar;
import java.util.concurrent.TimeUnit;

/**
 * Created by muthuveerappans on 9/14/17.
 */

/*
adb commands :
 + alarm dump: ./adb shell dumpsys alarm | grep <package name>
 */

public class RemainderActivity extends AppCompatActivity implements View.OnClickListener
        , TimePickerFragment.TimePickerCallback {
    Button submit, pickTime;
    EditText notesEdt;
    int hour, min;
    boolean fromNotification = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.remainder_activity);

        submit = (Button) findViewById(R.id.submit);
        pickTime = (Button) findViewById(R.id.pick_time);
        notesEdt = (EditText) findViewById(R.id.notes_edt);

        submit.setOnClickListener(this);
        pickTime.setOnClickListener(this);

        Bundle b = getIntent().getExtras();
        if (b != null) {
            Remainder remainder = b.getParcelable("data");
            hour = remainder.hour;
            min = remainder.min;
            notesEdt.setText(remainder.notes);
            pickTime.setText(new StringBuilder().append(pad(hour))
                    .append(":").append(pad(min)));
            submit.setVisibility(View.GONE);
            fromNotification = true;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                Intent upIntent = NavUtils.getParentActivityIntent(this);
                if (NavUtils.shouldUpRecreateTask(this, upIntent) || fromNotification) {
                    // This activity is NOT part of this app's task, so create a new task
                    // when navigating up, with a synthesized back stack.
                    TaskStackBuilder.create(this)
                            // Add all of this activity's parents to the back stack
                            .addNextIntentWithParentStack(upIntent)
                            // Navigate up to the closest parent
                            .startActivities();
                } else {
                    // This activity is part of this app's task, so simply
                    // navigate up to the logical parent activity.
                    NavUtils.navigateUpTo(this, upIntent);
                }
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.submit:
                scheduleNotification();
                finish();
                break;
            case R.id.pick_time:
                showTimePicker();
                break;
        }
    }

    private void showTimePicker() {
        DialogFragment newFragment = new TimePickerFragment();
        newFragment.show(getSupportFragmentManager(), "timePicker");
    }

    @Override
    public void getTime(int hourOfDay, int minute) {
        pickTime.setText(new StringBuilder().append(pad(hourOfDay))
                .append(":").append(pad(minute)));

        hour = hourOfDay;
        min = minute;
    }

    private static String pad(int c) {
        if (c >= 10)
            return String.valueOf(c);
        else
            return "0" + String.valueOf(c);
    }

    private void scheduleNotification() {
        String notes = notesEdt.getText().toString();
        Remainder remainder = new Remainder(notes, hour, min);

        /*
        The problem is that when AlarmThread#run invokes PendingIntent#send, a new Intent is created and populated with Intent#fillIn.
        https://android.googlesource.com/platform/frameworks/base/+/android-4.4.4_r2.0.1/services/java/com/android/server/am/PendingIntentRecord.java#210

        This makes extras bundle to be unparceled. Hence, if you have a custom Parcelable in that bundle, system process is not able to handle this since it cannot load a class from a different process.
        And wrapping your custom Parcelable with a Bundle works due to the fact that Bundle unmarshales its internals only when some key is directly accessed. So the system process does not load a class but just moves bytes around.
         */

        Intent alarmIntent = new Intent(this, AlarmReceiver.class);
        Bundle bundle = new Bundle();
        bundle.putParcelable("data", remainder);
        alarmIntent.putExtra("alarm_data", bundle);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, alarmIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        Calendar currentCalendar = Calendar.getInstance();

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, min);

        long currentTime = currentCalendar.getTimeInMillis();
        long futureTime = calendar.getTimeInMillis();

        long time = futureTime - currentTime;

        // test: set an alarm after 5 sec.
        time = 5000;
        futureTime = SystemClock.elapsedRealtime() + time;

        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        alarmManager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, futureTime, pendingIntent);
    }
}
