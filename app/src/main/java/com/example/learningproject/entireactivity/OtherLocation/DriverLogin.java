package com.example.learningproject.entireactivity.OtherLocation;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.example.learningproject.R;
import com.example.learningproject.databinding.ActivityDriverLoginBinding;
import com.example.learningproject.entireactivity.utils.Constants;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Objects;

public class DriverLogin extends AppCompatActivity {

    private ActivityDriverLoginBinding binding;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener firebaseAuthListener;
    private  String role;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
     binding=DataBindingUtil.setContentView(this,R.layout.activity_driver_login);
        // Get Role from Intent
        Bundle bundle=getIntent().getExtras();
        role=bundle.getString(Constants.IntentTypes.ROLE,null);
     setFirebase();
     setonClicks();
    }

    private void setFirebase() {
        ///FirebaseApp.initializeApp(this);
        mAuth = FirebaseAuth.getInstance();

        firebaseAuthListener = firebaseAuth -> {
            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            if(user!=null){
                Intent intent = new Intent(DriverLogin.this, DriverMapActivity.class);
                startActivity(intent);
                finish();
            }
        };

    }

    private void setonClicks() {

        binding.registration.setOnClickListener(v -> {
            final String email = binding.email.getText().toString();
            final String password = binding.password.getText().toString();
            mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(DriverLogin.this, task -> {
                if(!task.isSuccessful()){
                    Toast.makeText(DriverLogin.this, "sign up error", Toast.LENGTH_SHORT).show();
                }else{
                    String user_id = Objects.requireNonNull(mAuth.getCurrentUser()).getUid();
                    if (role != null && role.equals("Driver")) {
                        DatabaseReference current_user_db = FirebaseDatabase.getInstance().getReference().child("Users").child("Drivers").child(user_id).child("name");
                        current_user_db.setValue(email);

                    }


                }
            });
        });

        binding.login.setOnClickListener(v -> {
            final String email = binding.email.getText().toString();
            final String password = binding.password.getText().toString();
            mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(DriverLogin.this, task -> {
                if(!task.isSuccessful()){
                    Toast.makeText(DriverLogin.this, "sign in error", Toast.LENGTH_SHORT).show();
                }
            });

        });


    }


    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(firebaseAuthListener);
    }
    @Override
    protected void onStop() {
        super.onStop();
        mAuth.removeAuthStateListener(firebaseAuthListener);
    }
}