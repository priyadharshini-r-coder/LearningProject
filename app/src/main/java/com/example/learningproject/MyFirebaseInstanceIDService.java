package com.example.learningproject;


import android.annotation.SuppressLint;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

import com.google.firebase.iid.FirebaseInstanceIdReceiver;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

@SuppressLint("MissingFirebaseInstanceTokenRefresh")
public class MyFirebaseInstanceIDService  extends FirebaseMessagingService {
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
       String title= remoteMessage.getNotification().getTitle();
       String desc=remoteMessage.getNotification().getBody();
       final String CHANNEL_ID="NOTIFICATION_CHANNEL_ID";
        NotificationChannel channel=new NotificationChannel(CHANNEL_ID,"Notification Channel",NotificationManager.IMPORTANCE_HIGH);

    }
    getSystemService(NotificationManager.class)

}
