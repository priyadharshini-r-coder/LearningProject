package com.example.learningproject.entireactivity.MyLocation;

import com.google.firebase.auth.FirebaseAuth;

public class AuthService {
    public void userSignOut(){
        FirebaseAuth.getInstance().signOut();
    }

}
