package com.example.learningproject.newChanges.activity;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import com.example.learningproject.R;
import com.example.learningproject.databinding.ActivityMainBinding;
import com.example.learningproject.newChanges.utils.Constants;


public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       binding= DataBindingUtil. setContentView(this,R.layout.activity_main);
       setonClicks();

    }

    private void setonClicks() {
        binding.btnDriver.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, SignupActivity.class);
            intent.putExtra(Constants.IntentTypes.ROLE,"Driver");
            startActivity(intent);
            finish();
        });

        binding.btnCustomer.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, SignupActivity.class);
             intent.putExtra(Constants.IntentTypes.ROLE,"Customer");
            startActivity(intent);
            finish();
        });
    }
}