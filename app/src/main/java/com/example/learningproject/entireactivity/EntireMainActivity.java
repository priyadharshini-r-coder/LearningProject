package com.example.learningproject.entireactivity;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.example.learningproject.R;
import com.example.learningproject.databinding.ActivityEnitreMainBinding;
import com.example.learningproject.entireactivity.MyLocation.MyLoginCheck;
import com.example.learningproject.entireactivity.OtherLocation.DriverAuthActivity;

public class EntireMainActivity extends AppCompatActivity {

    private ActivityEnitreMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_enitre_main);
        setClicks();
    }

    private void setClicks() {
        binding.btnCustomer.setOnClickListener(view ->
                startActivity(new Intent(EntireMainActivity.this, MyLoginCheck.class)));

       binding.btnDriver.setOnClickListener(view -> {
           Intent intent = new Intent(EntireMainActivity.this, DriverAuthActivity.class);
           startActivity(intent);
           finish();
       });

    }
}