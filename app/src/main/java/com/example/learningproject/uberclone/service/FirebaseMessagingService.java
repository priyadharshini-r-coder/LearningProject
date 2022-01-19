package com.example.learningproject.uberclone.service;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import com.example.learningproject.R;
import com.example.learningproject.uberclone.activities.DriverMapsActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.messaging.RemoteMessage;

public class FirebaseMessagingService  extends com.google.firebase.messaging.FirebaseMessagingService {
    private static final String CHANNEL_ID_ARRIVED ="Channel_Arrived" ;

    @Override
    public void onMessageReceived(final RemoteMessage remoteMessage) {
        if(remoteMessage.getNotification().getTitle().equals("Cancel")){
            Handler handler = new Handler(Looper.getMainLooper());
            handler.post(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(FirebaseMessagingService.this,""+remoteMessage.getNotification().getBody(), Toast.LENGTH_SHORT).show();
                }
            });
        }else if(remoteMessage.getNotification().getTitle().equals("Accept")){
            try {
                Handler handler = new Handler(Looper.getMainLooper());
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        /*Intent intent = new Intent(getBaseContext(), .class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        intent.putExtra("keyDriver",remoteMessage.getNotification().getBody());
                        startActivity(intent);*/
                    }
                });
            }catch (Exception ex) {
                Toast.makeText(this, ""+ex, Toast.LENGTH_SHORT).show();
            }
        }else if(remoteMessage.getNotification().getTitle().equals("Arrived")){

            try {
                showArrivedNotification(remoteMessage.getNotification().getBody());

            }catch (Exception ex){
                Toast.makeText(this, ""+ex, Toast.LENGTH_SHORT).show();
            }
        }else if(remoteMessage.getNotification().getTitle().equals("DropOff")){
            //openRateActivity(remoteMessage.getNotification().getBody());
        }
    }

    @Override
    public void onNewToken(@NonNull String token) {
        super.onNewToken(token);
        FirebaseDatabase db=FirebaseDatabase.getInstance();
        DatabaseReference tokens=db.getReference().child("Customer Request").child("Token");

        if (FirebaseAuth.getInstance().getCurrentUser()!=null)
            tokens
                    .child(FirebaseAuth.getInstance().getUid())
                    .setValue(token);
    }


    private void showArrivedNotification(String body) {
        try {
            // Create the NotificationChannel, but only on API 26+ because
            // the NotificationChannel class is new and not in the support library
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                CharSequence name = getString(R.string.channel_arrived);
                String description = getString(R.string.channel_arrived_descrition);
                int importance = NotificationManager.IMPORTANCE_DEFAULT;
                NotificationChannel channel = new NotificationChannel(CHANNEL_ID_ARRIVED, name, importance);
                channel.setDescription(description);
                // Register the channel with the system; you can't change the importance
                // or other notification behaviors after this
                NotificationManager notificationManager = getSystemService(NotificationManager.class);
                notificationManager.createNotificationChannel(channel);
            }
            PendingIntent contentIntent = PendingIntent.getActivity(getBaseContext(),
                    0,new Intent(),PendingIntent.FLAG_ONE_SHOT);
            NotificationCompat.Builder builder = new NotificationCompat.Builder(getBaseContext());
            builder.setAutoCancel(true)
                    .setDefaults(Notification.DEFAULT_LIGHTS|Notification.DEFAULT_SOUND)
                    .setWhen(System.currentTimeMillis())
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setContentTitle("Arrived")
                    .setContentText(body)
                    .setContentIntent(contentIntent)
                    .setChannelId(CHANNEL_ID_ARRIVED);
            NotificationManager manager = (NotificationManager)getBaseContext().getSystemService(Context.NOTIFICATION_SERVICE);
            manager.notify(1,builder.build());

            Intent intent = new Intent(this, DriverMapsActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }catch (Exception ex) {
            Toast.makeText(this, ""+ex, Toast.LENGTH_SHORT).show();
        }
    }
}
