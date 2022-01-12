package com.example.learningproject.uberclone.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.os.Bundle;
import android.view.View;

import com.example.learningproject.R;
import com.example.learningproject.databinding.ActivityDriverLoginRegisterBinding;
import com.google.firebase.auth.FirebaseAuth;

public class DriverLoginRegister extends AppCompatActivity {

    private ActivityDriverLoginRegisterBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       binding= DataBindingUtil .setContentView(this,R.layout.activity_driver_login_register);

        setOnClicks();
    }

    private void setOnClicks() {
        binding.button3.setVisibility(View.INVISIBLE);
        binding.button3.setEnabled(false);

        binding.Account.setOnClickListener(view -> {
            binding.button3.setVisibility(View.VISIBLE);
            binding.buttonLogins.setVisibility(View.INVISIBLE);
            binding.Account.setVisibility(View.INVISIBLE);
            binding.DriverTitle.setText("Customer Register");
            binding.button3.setEnabled(true);
        });


        binding.buttonLogins.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email= binding.editTextemail.getText().toString();
                String password= binding.editTextPassword.getText().toString();
             //   mAuth= FirebaseAuth.getInstance().getCurrentUser().getUid();

            }
        });

    }
}