package com.example.learningproject.entireactivity.MyLocation;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import com.example.learningproject.R;
import com.example.learningproject.databinding.ActivityMyLoginCheckBinding;
import com.example.learningproject.entireactivity.utils.Constants;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import java.util.Objects;

public class MyLoginCheck extends AppCompatActivity {
  // Variables
    private ActivityMyLoginCheckBinding binding;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener authStateListener;
    private String role;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //DataBinding
      binding= DataBindingUtil.setContentView(this,R.layout.activity_my_login_check);

      // Get Role from Intent
      Bundle bundle=getIntent().getExtras();
      role=bundle.getString(Constants.IntentTypes.ROLE,null);

      setAuthentication();
      setClicks();
    }

    private void setAuthentication() {
        mAuth = FirebaseAuth.getInstance();
        // If Already login then
        authStateListener= firebaseAuth -> {
            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            if (user != null) {
                Intent intent = new Intent(MyLoginCheck.this, MyMapActivity.class);
                startActivity(intent);
                finish();
            }
        };
    }

    private void setClicks() {
     binding.btnSignup.setOnClickListener(view -> {
         final String email = binding.etEmail.getText().toString();
         final String password = binding.etPassword.getText().toString();
       mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(MyLoginCheck.this, task -> {
           if (!task.isSuccessful()) {
               Toast.makeText(MyLoginCheck.this, "Sign up error : " + Objects.requireNonNull(task.getException()).getMessage(), Toast.LENGTH_SHORT).show();
           } else {

               String user_id = Objects.requireNonNull(mAuth.getCurrentUser()).getUid();

               DatabaseReference user_db;

               if (role != null && role.equals("Customer")) {
                   user_db = FirebaseDatabase.getInstance().getReference()
                           .child("Users").child("Customers").child(user_id);

               }
              else   {
                   user_db = FirebaseDatabase.getInstance().getReference()
                           .child("Users").child("Drivers").child(user_id);

               }
               user_db.setValue(email);
              user_db.setValue(password);

           }
       });

     });

     binding.btnLogin.setOnClickListener(view -> {
         final String email = binding.etEmail.getText().toString();
         final String password = binding.etPassword.getText().toString();
         mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(MyLoginCheck.this, task -> {
             if (!task.isSuccessful()) {
                 Toast.makeText(MyLoginCheck.this, "Login error : " + Objects.requireNonNull(task.getException()).getMessage(), Toast.LENGTH_SHORT).show();
             }
             else {
                 String user_id = Objects.requireNonNull(mAuth.getCurrentUser()).getUid();
                 DatabaseReference user_db;

                 if(role!=null && role.equals("Customer")) {
                     user_db = FirebaseDatabase.getInstance().getReference()
                             .child("Users").child("Customers").child(user_id);
                 }
                 else{
                     user_db = FirebaseDatabase.getInstance().getReference()
                             .child("Users").child("Drivers").child(user_id);
                 }
                 user_db.setValue(email);
                 user_db.setValue(password);
             }
         });
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