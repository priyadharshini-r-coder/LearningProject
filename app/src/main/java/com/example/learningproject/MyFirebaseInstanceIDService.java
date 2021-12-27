package com.example.learningproject;


import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationManagerCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Objects;

@SuppressLint("MissingFirebaseInstanceTokenRefresh")
public class MyFirebaseInstanceIDService  extends FirebaseMessagingService {
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
       String title= Objects.requireNonNull(remoteMessage.getNotification()).getTitle();
       String desc=remoteMessage.getNotification().getBody();
       final String CHANNEL_ID="NOTIFICATION_CHANNEL_ID";
        NotificationChannel channel=new NotificationChannel(CHANNEL_ID,"Notification Channel",NotificationManager.IMPORTANCE_HIGH);
         getSystemService(NotificationManager.class).createNotificationChannel(channel);
        Notification.Builder notification=new Notification.Builder(this,CHANNEL_ID)
                .setContentTitle(title)
                .setContentText(desc).setSmallIcon(R.drawable.ic_launcher_background)
                .setAutoCancel(true);
        NotificationManagerCompat.from(this).notify(1,notification.build());
        super.onMessageReceived(remoteMessage);

    }


}
