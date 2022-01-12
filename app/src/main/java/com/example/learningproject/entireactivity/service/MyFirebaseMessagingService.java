package com.example.learningproject.entireactivity.service;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import com.example.learningproject.R;
import com.example.learningproject.entireactivity.EntireMainActivity;
import com.example.learningproject.entireactivity.OtherLocation.DriverLogin;
import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.google.gson.Gson;

public class MyFirebaseMessagingService  extends FirebaseMessagingService {
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

        Log.d("EDMTDEV" ,remoteMessage.getNotification().getBody());

        //The LatLong cordinates will then come from the rider app
        LatLng customer_location = new Gson().fromJson(remoteMessage.getNotification().getBody(),LatLng.class);

        Intent intent = new Intent (getBaseContext() , DriverLogin.class);//getBaseContext()
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);// DELETE THIS PERHAPS WHEN YOUR PHONE RUNS ANDROID 7 ...LOL
        intent.putExtra("lat" , customer_location.latitude);
        intent.putExtra("lng" , customer_location.longitude );
        intent.putExtra("customer" , remoteMessage.getNotification().getTitle());

        getBaseContext().startActivity(intent);




    }
}