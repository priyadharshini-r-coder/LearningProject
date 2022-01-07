package com.example.learningproject.newChanges.services;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Looper;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;

import com.example.learningproject.R;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Objects;

public class MyFirebaseMessaging extends FirebaseMessagingService {
    @Override
    public void onMessageReceived(final RemoteMessage remoteMessage) {

        if (Objects.equals(Objects.requireNonNull(remoteMessage.getNotification()).getTitle(), "Cancel"))
        {

            android.os.Handler handler = new android.os.Handler(Looper.getMainLooper());
            handler.post(() -> Toast.makeText(MyFirebaseMessaging.this, ""+remoteMessage.getNotification().getBody(), Toast.LENGTH_SHORT).show());


        }


        else if (Objects.equals(remoteMessage.getNotification().getTitle(), "Arrived"))
        {

            showArrivedNotification(remoteMessage.getNotification().getBody());



        }



    }

    private void showArrivedNotification(String body) {
        //this will work for API 25 and below so we will make it compatible later on
        PendingIntent contentIntent = PendingIntent.getActivity(getBaseContext(),
                0 , new Intent() ,  PendingIntent.FLAG_ONE_SHOT);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(getBaseContext());

        builder.setAutoCancel(true)
                .setDefaults(android.app.Notification.DEFAULT_LIGHTS| android.app.Notification.DEFAULT_SOUND)
                .setWhen(System.currentTimeMillis())
                .setSmallIcon(R.mipmap.ic_launcher_round)
                .setContentTitle("Arrived")
                .setContentText(body)
                .setContentIntent(contentIntent);
        NotificationManager manager = (NotificationManager)getBaseContext().getSystemService(Context.NOTIFICATION_SERVICE);
        manager.notify(1,builder.build());


    }
}
