package com.example.learningproject.entireactivity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RadioButton;

import com.example.learningproject.R;
import com.example.learningproject.databinding.ActivityEnitreMainBinding;
import com.example.learningproject.entireactivity.MyLocation.MyLoginCheck;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class EnitreMainActivity extends AppCompatActivity {

  private ActivityEnitreMainBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
      binding= DataBindingUtil.setContentView(this,R.layout.activity_enitre_main);
       setClicks();
    }

    private void setClicks() {
        binding.btnCustomer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(EnitreMainActivity.this, MyLoginCheck.class));
            }
        });
    }


}