package com.example.learningproject.parsers.services;


import com.example.learningproject.parsers.common.CommonUrl;
import com.example.learningproject.parsers.model.Token;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MyFirebaseIdService {
    /*@Override
    public void onTokenRefresh() {
        super.onTokenRefresh();
        String refreshToken = FirebaseInstanceId.getInstance().getToken();
        updateTokenToServer(refreshToken);
    }*/

    private void updateTokenToServer(String refreshToken) {
        FirebaseDatabase db = FirebaseDatabase.getInstance();
        DatabaseReference tokens = db.getReference(CommonUrl.token_tb1);

        Token token = new Token(refreshToken);
        if (FirebaseAuth.getInstance().getCurrentUser() != null) // if already login , must update Token
            tokens.child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                    .setValue(token);



    }

}
