package com.example.learningproject.entireactivity.MyLocation;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.learningproject.R;
import com.example.learningproject.databinding.ActivityMyLoginCheckBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MyLoginCheck extends AppCompatActivity {

    private ActivityMyLoginCheckBinding binding;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener authStateListener;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
      binding= DataBindingUtil.setContentView(this,R.layout.activity_my_login_check);

      setAuthentication();
      setClicks();
    }

    private void setAuthentication() {
        mAuth = FirebaseAuth.getInstance();
        authStateListener=new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                if (user != null) {
                    Intent intent = new Intent(MyLoginCheck.this, MyMapActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        };
    }

    private void setClicks() {
     binding.btnSignup.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View view) {
             final String email = binding.etEmail.getText().toString();
             final String password = binding.etPassword.getText().toString();
           mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(MyLoginCheck.this, new OnCompleteListener<AuthResult>() {
               @Override
               public void onComplete(@NonNull Task<AuthResult> task) {
                   if (!task.isSuccessful()) {
                       Toast.makeText(MyLoginCheck.this, "Sign up error : " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                   } else {
                       String user_id = mAuth.getCurrentUser().getUid();
                       DatabaseReference user_db = FirebaseDatabase.getInstance().getReference()
                               .child("Users").child("Customers").child(user_id);
                       user_db.setValue(true);
                   }
               }
           });

         }
     });

     binding.btnLogin.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View view) {
             final String email = binding.etEmail.getText().toString();
             final String password = binding.etPassword.getText().toString();
             mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(MyLoginCheck.this, new OnCompleteListener<AuthResult>() {
                 @Override
                 public void onComplete(@NonNull Task<AuthResult> task) {
                     if (!task.isSuccessful()) {
                         Toast.makeText(MyLoginCheck.this, "Login error : " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                     }
                 }
             });
         }
     });
    }

    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(authStateListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        mAuth.removeAuthStateListener(authStateListener);
    }
}