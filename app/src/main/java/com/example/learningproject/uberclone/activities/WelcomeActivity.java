package com.example.learningproject.uberclone.activities;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import com.example.learningproject.R;
import com.example.learningproject.databinding.ActivityWelcomeBinding;

public class WelcomeActivity extends AppCompatActivity {
     private ActivityWelcomeBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       binding= DataBindingUtil. setContentView(this,R.layout.activity_welcome);
       setOnClicks();
    }

    private void setOnClicks() {
        binding.buttonCustomer.setOnClickListener(view -> {
            Intent intent= new Intent(WelcomeActivity.this,CustomerLoginRegister.class);
            startActivity(intent);
        });

        binding.buttonDriver.setOnClickListener(view -> {
            Intent intent= new Intent(WelcomeActivity.this,DriverLoginRegister.class);
            startActivity(intent);
        });
    }
}