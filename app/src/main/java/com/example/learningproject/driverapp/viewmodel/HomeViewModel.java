package com.example.learningproject.driverapp.viewmodel;

import android.app.Activity;
import android.content.Intent;

import com.example.learningproject.driverapp.activity.LoginActivity;
import com.google.firebase.auth.FirebaseAuth;

public class HomeViewModel {
    public HomeViewModel() {

    }

    public void signOut(Activity activity) {
        FirebaseAuth.getInstance().signOut();
        Intent intent=new Intent(activity, LoginActivity.class);
        activity.startActivity(intent);
        activity.finish();
    }
}
