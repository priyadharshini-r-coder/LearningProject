package com.example.learningproject.entireactivity;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.example.learningproject.R;
import com.example.learningproject.databinding.ActivityEnitreMainBinding;
import com.example.learningproject.entireactivity.MyLocation.CustomerLogin;
import com.example.learningproject.entireactivity.OtherLocation.DriverLogin;
import com.example.learningproject.entireactivity.apiRetrofit.CommonUrl;
import com.example.learningproject.entireactivity.utils.Constants;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class EntireMainActivity extends AppCompatActivity {

    private ActivityEnitreMainBinding binding;
    DatabaseReference mUsers;
    FirebaseAuth mAuth;
    FirebaseDatabase mDB;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       binding=DataBindingUtil. setContentView(this,R.layout.activity_enitre_main);

         setDatabase();
        setOnClicks();

    }
    private void setDatabase() {
        mAuth = FirebaseAuth.getInstance();
        mDB = FirebaseDatabase.getInstance();
        mUsers = mDB.getReference(CommonUrl.user_driver_tb1);
    }
    private void setOnClicks() {
        binding.btnDriver.setOnClickListener(v -> {
            Intent intent = new Intent(EntireMainActivity.this, DriverLogin.class);
            intent.putExtra(Constants.IntentTypes.ROLE,"Driver");
            startActivity(intent);
            finish();
        });

        binding.btnCustomer.setOnClickListener(v -> {
            Intent intent = new Intent(EntireMainActivity.this, CustomerLogin.class);
          //  intent.putExtra(Constants.IntentTypes.ROLE,"Customer");
            startActivity(intent);
            finish();
        });
    }


}