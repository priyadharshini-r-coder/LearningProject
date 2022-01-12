package com.example.learningproject.uberclone.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.os.Bundle;
import android.view.View;

import com.example.learningproject.R;
import com.example.learningproject.databinding.ActivityCustomerLoginRegisterBinding;
import com.google.firebase.auth.FirebaseAuth;

public class CustomerLoginRegister extends AppCompatActivity {
    private ActivityCustomerLoginRegisterBinding binding;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding= DataBindingUtil.setContentView(this,R.layout.activity_customer_login_register);

       setOnClicks();
    }

    private void setOnClicks() {
        binding.button3.setVisibility(View.INVISIBLE);
        binding.button3.setEnabled(false);

        binding.Account.setOnClickListener(view -> {
            binding.button3.setVisibility(View.VISIBLE);
            binding.buttonLogins.setVisibility(View.INVISIBLE);
            binding.Account.setVisibility(View.INVISIBLE);
            binding.CustomerTitle.setText("Customer Register");
            binding.button3.setEnabled(true);
        });


        binding.buttonLogins.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email= binding.editTextemail.getText().toString();
                String password= binding.editTextPassword.getText().toString();
                mAuth=FirebaseAuth.getInstance().getCurrentUser().getUid();

            }
        });

    }
}