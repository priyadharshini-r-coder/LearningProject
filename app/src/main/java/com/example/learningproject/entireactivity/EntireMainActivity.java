package com.example.learningproject.entireactivity;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import com.example.learningproject.R;
import com.example.learningproject.databinding.ActivityEnitreMainBinding;
import com.example.learningproject.entireactivity.MyLocation.MyLoginCheck;
import com.example.learningproject.entireactivity.utils.Constants;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class EntireMainActivity extends AppCompatActivity {

    private ActivityEnitreMainBinding binding;
    FirebaseAuth mAuth;
    FirebaseDatabase mDB;
    DatabaseReference mUsers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_enitre_main);
        setDatabase();
        setClicks();
    }

    private void setDatabase() {
        mAuth = FirebaseAuth.getInstance();
        mDB = FirebaseDatabase.getInstance();
        mUsers = mDB.getReference("Users");
    }

    private void setClicks() {
        binding.btnCustomer.setOnClickListener(view -> {
            Intent intent = new Intent(EntireMainActivity.this, MyLoginCheck.class);
            intent.putExtra(Constants.IntentTypes.ROLE,"Customer");
            startActivity(intent);
            finish();
        });

       binding.btnDriver.setOnClickListener(view -> {
           Intent intent = new Intent(EntireMainActivity.this, MyLoginCheck.class);
           intent.putExtra(Constants.IntentTypes.ROLE,"Driver");
           startActivity(intent);
           finish();
       });

    }
}