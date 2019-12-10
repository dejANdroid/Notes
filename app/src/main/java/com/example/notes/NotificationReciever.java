package com.example.notes;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import androidx.core.app.NotificationCompat;
import androidx.room.Database;

import com.example.notes.RoomDb.DataBase;
import com.example.notes.model.Note;

import java.util.Random;

public class NotificationReciever extends BroadcastReceiver {

    public static final String CHANNEL_ID = "channel_id";int randomId;

    @Override
    public void onReceive(Context context, Intent intent) {

        NotificationManager notificationManager = (NotificationManager) context
                .getSystemService(Context.NOTIFICATION_SERVICE);

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {

            NotificationChannel channel = new NotificationChannel
                    (CHANNEL_ID, "note_notification", NotificationManager.IMPORTANCE_DEFAULT);

            if (notificationManager != null) {

                notificationManager.createNotificationChannel(channel);
            }
        }

        String note = intent.getStringExtra("NOTE_TEXT");
        String date = intent.getStringExtra("DATE_TEXT");

        Random random = new Random();
        randomId = random.nextInt(1000 - 99) + 99;

        Intent openNotification = new Intent(context, ReadNoteActivity.class);
        openNotification.putExtra("NOTE", note);
        openNotification.putExtra("DATE", date);

        Intent backIntent = new Intent(context, MainActivity.class);
        backIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        final PendingIntent resultPendingIntent = PendingIntent.getActivities(
                context, randomId,
                new Intent[]{backIntent,openNotification}, PendingIntent.FLAG_ONE_SHOT);

        Notification notification = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setContentTitle("Note Reminder !")
                .setContentText("You write Note for today." + "\n" + "Click to read the note.")
                .setSmallIcon(R.drawable.ic_event_note_black_24dp)
                .setContentIntent(resultPendingIntent)
                .setAutoCancel(true)
                .build();

        if (notificationManager != null) {
            notificationManager.notify(randomId, notification);
        }
    }
}
