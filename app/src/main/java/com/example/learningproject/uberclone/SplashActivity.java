package com.example.learningproject.uberclone;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.example.learningproject.R;
import com.example.learningproject.databinding.ActivitySplashBinding;
import com.example.learningproject.uberclone.activities.WelcomeActivity;

public class SplashActivity extends AppCompatActivity {

   private ActivitySplashBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
         binding= DataBindingUtil.setContentView(this, R.layout.activity_splash);
       Thread thread= new Thread(() -> {
           doWork();
           startApp();
           finish();
       });
       thread.start();

    }

    private void doWork() {
        for (int progress=0; progress<100; progress+=10) {
            try {
                Thread.sleep(100);
                binding.progressBar.setProgress(progress);
            } catch (Exception e) {
                e.printStackTrace();
                Log.d("Splash ERROR",e.getMessage());
            }
        }
    }

    private void startApp() {
        Intent intent = new Intent(SplashActivity.this, WelcomeActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onPause() {
        super.onPause();
        finish();

    }
}