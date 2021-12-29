package com.example.learningproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.learningproject.databinding.ActivityMainBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.Objects;

import javax.xml.transform.Result;

public class MainActivity extends AppCompatActivity {
public ActivityMainBinding binding;
String token;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding= DataBindingUtil.setContentView(this,R.layout.activity_main);

        FirebaseMessaging.getInstance().subscribeToTopic("all");
     /*   FirebaseInstanceId.getInstance().getInstanceId().
                addOnCompleteListener(new OnCompleteListener<Result>() {
                    @Override
                    public void onComplete(@NonNull Task<Result> task) {
                        if(task.isSuccessful())
                        {
                        token = Objects.requireNonNull(task.getResult().getToken());
                        Log.d("Tag","token" +token);

                        }
                    }
                });*/

      binding.sendNotification.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View v) {
  if(!binding.title.getText().toString().isEmpty() &&
       !binding.message.getText().toString().isEmpty())
  {
   /*  FcmNotificationSender notificationSender=new FcmNotificationSender(
             "/topics/all",binding.title.getText().toString(),
             binding.message.getText().toString(),getApplicationContext(),MainActivity.this);
     notificationSender.SendNotifications();*/
  }
  else
  {
      Toast.makeText(MainActivity.this,"Please enter Details",Toast.LENGTH_LONG).show();
  }

         }
     });

      binding.sendNotificationParticularuser.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
             if(!binding.title.getText().toString().isEmpty()  &&
            !binding.message.getText().toString().isEmpty() && !binding.userToken.getText().toString().isEmpty())
             {

               /*  FcmNotificationSender notificationSender = new FcmNotificationSender(binding.userToken.getText().toString(),binding.title.getText().toString(),
                         binding.message.getText().toString(),getApplicationContext(),MainActivity.this);
                 notificationSender.SendNotifications();*/
             }
             else
             {
                 Toast.makeText(MainActivity.this,"Please Enter Details",Toast.LENGTH_LONG).show();
             }
          }
      });
    }


}