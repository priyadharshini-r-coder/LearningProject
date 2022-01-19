package com.example.learningproject.uberclone.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.example.learningproject.R;
import com.example.learningproject.databinding.ActivityCustomerLoginRegisterBinding;
import com.example.learningproject.uberclone.model.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Objects;

public class CustomerLoginRegister extends AppCompatActivity {
    private ActivityCustomerLoginRegisterBinding binding;
    private FirebaseAuth mAuth;
    ProgressDialog progressDialog;
    private DatabaseReference customerDatabaseRef;
    private  String onlineCustomerId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding= DataBindingUtil.setContentView(this,R.layout.activity_customer_login_register);
        progressDialog=new ProgressDialog(this);
        mAuth=FirebaseAuth.getInstance();

       setOnClicks();
    }

    private void setOnClicks() {
        binding.button3.setVisibility(View.INVISIBLE);
        binding.button3.setEnabled(false);

        binding.Account.setOnClickListener(view -> {
            binding.button3.setVisibility(View.VISIBLE);
            binding.buttonLogins.setVisibility(View.INVISIBLE);
            binding.Account.setVisibility(View.INVISIBLE);
            binding.CustomerTitle.setText(R.string.customer_register);
            binding.button3.setEnabled(true);
        });


        binding.button3.setOnClickListener(view -> {
            String email= binding.editTextemail.getText().toString();
            String password= binding.editTextPassword.getText().toString();
             registerCustomer(email,password);

        });
        binding.buttonLogins.setOnClickListener(v -> {
            String email= binding.editTextemail.getText().toString();
            String password= binding.editTextPassword.getText().toString();
            loginCustomer(email,password);
        });

    }

    private void loginCustomer(String email, String password) {
        if (TextUtils.isEmpty(email)) {
            Toast.makeText(CustomerLoginRegister.this, "Please enter Email", Toast.LENGTH_LONG).show();

        }
        if (TextUtils.isEmpty(password)) {
            Toast.makeText(CustomerLoginRegister.this, "Please enter Password", Toast.LENGTH_LONG).show();

        } else {
            progressDialog.setTitle("Customer  Login");
            progressDialog.setMessage("Please wait ,while we are checking your credentials...");
            progressDialog.show();
            mAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            Toast.makeText(CustomerLoginRegister.this, "Customer Logged In Successful", Toast.LENGTH_LONG).show();
                            progressDialog.dismiss();
                            startActivity(new Intent(CustomerLoginRegister.this,CustomerMapsActivity.class));
                        } else {
                            Toast.makeText(CustomerLoginRegister.this, "Customer Login UnSuccessful,PLease try again", Toast.LENGTH_LONG).show();
                            progressDialog.dismiss();
                            startActivity(new Intent(CustomerLoginRegister.this,WelcomeActivity.class));
                        }
                    });
        }
    }


    private void registerCustomer(String email,String password) {
        if(TextUtils.isEmpty(email))
        {
            Toast.makeText(CustomerLoginRegister.this,"Please enter Email",Toast.LENGTH_LONG).show();

        }
        if(TextUtils.isEmpty(password))
        {
            Toast.makeText(CustomerLoginRegister.this,"Please enter Password",Toast.LENGTH_LONG).show();

        }
        else
        {
            progressDialog.setTitle("Customer  Register");
            progressDialog.setMessage("Please wait ,while we are register your data...");
            progressDialog.show();

            mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(
                    task -> {
                        if(task.isSuccessful())
                        {
                            onlineCustomerId= Objects.requireNonNull(mAuth.getCurrentUser()).getUid();
                            User user=new User();
                            user.setEmail(email);
                            user.setPassword(password);
                            customerDatabaseRef= FirebaseDatabase.getInstance().getReference().child("Users")
                                    .child("Customers").child(onlineCustomerId);

                            customerDatabaseRef.setValue(user);

                            Toast.makeText(CustomerLoginRegister.this,"Customer Registered Successful",Toast.LENGTH_LONG).show();
                            progressDialog.dismiss();
                            startActivity(new Intent(CustomerLoginRegister.this,CustomerMapsActivity.class));

                        }
                        else
                        {
                            Toast.makeText(CustomerLoginRegister.this,"Customer Registered UnSuccessful,PLease try again",Toast.LENGTH_LONG).show();
                            progressDialog.dismiss();
                           // startActivity(new Intent(CustomerLoginRegister.this,WelcomeActivity.class));
                        }
                    }
            );

        }
    }

    }
