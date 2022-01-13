package com.example.learningproject.uberclone.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import com.example.learningproject.R;
import com.example.learningproject.databinding.ActivityDriverLoginRegisterBinding;
import com.example.learningproject.uberclone.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.sql.Driver;

public class DriverLoginRegister extends AppCompatActivity {

    private ActivityDriverLoginRegisterBinding binding;
    FirebaseAuth auth;
    ProgressDialog progressDialog;
    DatabaseReference driverDatabaseRef;
    private String onlineDriverId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       binding= DataBindingUtil .setContentView(this,R.layout.activity_driver_login_register);
        auth=FirebaseAuth.getInstance();

        setOnClicks();
        progressDialog=new ProgressDialog(this);
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



        binding.button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email= binding.editTextemail.getText().toString();
                String password= binding.editTextPassword.getText().toString();
                registerDriver(email,password);
            }
        });
        binding.buttonLogins.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String  email=binding.editTextemail.getText().toString();
                String password=binding.editTextPassword.getText().toString();
                        loginDriver(email,password);
            }
        });

    }

    private void loginDriver(String email, String password) {
        if (TextUtils.isEmpty(email)) {
            Toast.makeText(DriverLoginRegister.this, "Please enter Email", Toast.LENGTH_LONG).show();

        }
        if (TextUtils.isEmpty(password)) {
            Toast.makeText(DriverLoginRegister.this, "Please enter Password", Toast.LENGTH_LONG).show();

        } else {
            progressDialog.setTitle("Customer  Login");
            progressDialog.setMessage("Please wait ,while we are checking your credentials...");
            progressDialog.show();
            auth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(DriverLoginRegister.this, "Driver LoggedIn Successful", Toast.LENGTH_LONG).show();
                                progressDialog.dismiss();
                                startActivity(new Intent(DriverLoginRegister.this,DriverMapsActivity.class));
                                finish();
                            } else {
                                Toast.makeText(DriverLoginRegister.this, "Driver Login UnSuccessful,PLease try again", Toast.LENGTH_LONG).show();
                                progressDialog.dismiss();
                            }
                        }

                    });
        }
    }

    private void registerDriver(String email, String password) {
        if(TextUtils.isEmpty(email))
        {
            Toast.makeText(DriverLoginRegister.this,"Please enter Email",Toast.LENGTH_LONG).show();

        }
        if(TextUtils.isEmpty(password))
        {
            Toast.makeText(DriverLoginRegister.this,"Please enter Password",Toast.LENGTH_LONG).show();

        }
        else
        {
            progressDialog.setTitle("Driver Register");
            progressDialog.setMessage("Please wait ,while we are register your data...");
            progressDialog.show();

            auth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(
                    new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                          if(task.isSuccessful())
                          {
                              onlineDriverId= auth.getCurrentUser().getUid();
                              User user=new User();
                              user.setEmail(email);
                              user.setPassword(password);
                              driverDatabaseRef= FirebaseDatabase.getInstance().getReference().child("Users")
                                      .child("Drivers").child(onlineDriverId);
                              driverDatabaseRef.setValue(user);
                              startActivity(new Intent(DriverLoginRegister.this,DriverMapsActivity.class));
                              Toast.makeText(DriverLoginRegister.this,"Driver Registered Successful",Toast.LENGTH_LONG).show();
                              progressDialog.dismiss();
                              finish();
                          }
                          else
                          {
                              Toast.makeText(DriverLoginRegister.this,"Driver Registered UnSuccessful,PLease try again",Toast.LENGTH_LONG).show();
                              progressDialog.dismiss();
                             // startActivity(new Intent(DriverLoginRegister.this,DriverMapsActivity.class));
                          }
                        }
                    }
            );

        }
    }
}