package com.example.learningproject.entireactivity;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.example.learningproject.R;
import com.example.learningproject.databinding.ActivityEnitreMainBinding;
import com.example.learningproject.entireactivity.MyLocation.CustomerLogin;
import com.example.learningproject.entireactivity.OtherLocation.DriverLogin;

public class EntireMainActivity extends AppCompatActivity {

    private ActivityEnitreMainBinding binding;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       binding=DataBindingUtil. setContentView(this,R.layout.activity_enitre_main);
        setOnClicks();

    }

    private void setOnClicks() {
        binding.btnDriver.setOnClickListener(v -> {
            Intent intent = new Intent(EntireMainActivity.this, DriverLogin.class);
           // intent.putExtra(Constants.IntentTypes.ROLE,"Driverss");
            startActivity(intent);
            finish();
        });

        binding.btnCustomer.setOnClickListener(v -> {
            Intent intent = new Intent(EntireMainActivity.this, CustomerLogin.class);
          // intent.putExtra(Constants.IntentTypes.ROLE,"Customerss");
            startActivity(intent);
            finish();
        });
    }


}