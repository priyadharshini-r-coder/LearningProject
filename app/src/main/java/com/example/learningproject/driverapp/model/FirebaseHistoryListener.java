package com.example.learningproject.driverapp.model;


import com.example.learningproject.driverapp.model.firebase.History;

import java.util.ArrayList;

public interface FirebaseHistoryListener {
    interface GetFirebaseHistoryListener {
        void onFirebaseHistoryRetrieved(ArrayList<History> historyList);
    }
}
