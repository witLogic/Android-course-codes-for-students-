package com.course4.notesapp.Remainder;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;

import com.course4.notesapp.Constants;
import com.course4.notesapp.List.NotesListActivity;
import com.course4.notesapp.R;

/**
 * Created by muthuveerappans on 9/15/17.
 */

public class AlarmReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Remainder remainder = intent.getBundleExtra("alarm_data").getParcelable("data");
        sendNotification(remainder, context);
    }

    private void sendNotification(Remainder remainder, Context context) {
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(context)
                        .setSmallIcon(R.mipmap.ic_launcher_round)
                        .setContentTitle("Remainder")
                        .setContentText(remainder.notes)
                        .setAutoCancel(true)
                        .setChannel(Constants.NOTIFICATION_CHANNEL_ID);

        Intent topStackIntent = new Intent(context, RemainderActivity.class);
        topStackIntent.putExtra("data", remainder);

        // Fake activity stack.
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
        // Add the activity parent chain as specified by manifest <meta-data> elements to the task stack builder.
        stackBuilder.addParentStack(RemainderActivity.class);
        stackBuilder.addNextIntent(topStackIntent);

        PendingIntent pendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
        mBuilder.setContentIntent(pendingIntent);
        NotificationManager mNotificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        mNotificationManager.notify(1, mBuilder.build());
    }
}
