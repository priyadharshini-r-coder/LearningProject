package com.example.learningproject.driverapp.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.learningproject.R;
import com.example.learningproject.driverapp.helper.FirebaseHelper;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {

    private Button btnSignIn, btnLogIn;

    FirebaseHelper firebaseHelper;
    GoogleSignInAccount account;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        firebaseHelper = new FirebaseHelper(this);
        initViews();
        initListeners();
    }

    private void initViews() {
        btnSignIn = findViewById(R.id.btnSignin);
        btnLogIn = findViewById(R.id.btnLogin);
    }

    private void initListeners() {
        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                firebaseHelper.showRegistrerDialog();
            }
        });
        btnLogIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                firebaseHelper.showLoginDialog();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        if(FirebaseAuth.getInstance().getCurrentUser() != null &&
                !FirebaseAuth.getInstance().getCurrentUser().getUid().equals("")){
            startActivity(new Intent(LoginActivity.this, HomeActivity.class));
            finish();
        }
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }

}