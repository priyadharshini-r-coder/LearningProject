package com.example.learningproject.entireactivity.MyLocation;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.example.learningproject.R;
import com.example.learningproject.databinding.ActivityCustomerLoginBinding;
import com.example.learningproject.entireactivity.OtherLocation.DriverMapActivity;
import com.example.learningproject.entireactivity.apiRetrofit.CommonUrl;
import com.example.learningproject.entireactivity.model.SigningReport;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Objects;

public class CustomerLogin extends AppCompatActivity {


    private ActivityCustomerLoginBinding binding;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener firebaseAuthListener;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=DataBindingUtil.setContentView(this,R.layout.activity_customer_login);

        setDb();
        setClick();



    }

    private void setDb() {

        mAuth = FirebaseAuth.getInstance();

        firebaseAuthListener = firebaseAuth -> {
            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            if(user!=null){
                Intent intent = new Intent(CustomerLogin.this,CustomerMap.class);
                startActivity(intent);
                finish();
            }
        };

    }

    private void setClick() {
        binding.btnSignup.setOnClickListener(v -> {
            final String email = binding.etEmail.getText().toString();
            final String password = binding.etPassword.getText().toString();
            SigningReport report= new SigningReport();
            report.setEmail(email);
            report.setPassword(password);

            mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(CustomerLogin.this, task -> {
                if(!task.isSuccessful()){
                    Toast.makeText(CustomerLogin.this, "sign up error", Toast.LENGTH_SHORT).show();
                }else{
                    String user_id = Objects.requireNonNull(mAuth.getCurrentUser()).getUid();

                    DatabaseReference current_user_db = FirebaseDatabase.getInstance().getReference(CommonUrl.user_driver_tb1).child(FirebaseAuth.getInstance().getCurrentUser().getUid());
                    current_user_db.setValue(report);
                }
            });
        });

        binding.btnLogin.setOnClickListener(v -> {
            final String email = binding.etEmail.getText().toString();
            final String password = binding.etPassword.getText().toString();
            mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(CustomerLogin.this, task -> {
                if(!task.isSuccessful()){
                    Toast.makeText(CustomerLogin.this, "sign in error", Toast.LENGTH_SHORT).show();
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