package com.example.learningproject.uberclone;

import static java.lang.Thread.sleep;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import com.example.learningproject.R;
import com.example.learningproject.uberclone.activities.WelcomeActivity;

public class SplashActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        com.example.learningproject.databinding.ActivitySplashBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_splash);
       Thread thread= new Thread(() -> {
           try{
               sleep(7000);
           }catch(Exception e){
               e.printStackTrace();
           }
           finally {
               startActivity(new Intent(SplashActivity.this, WelcomeActivity.class));
           }
       });
       thread.start();

    }

    @Override
    protected void onPause() {
        super.onPause();
        finish();

    }
}