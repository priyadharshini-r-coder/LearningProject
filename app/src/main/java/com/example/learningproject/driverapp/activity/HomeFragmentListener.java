package com.example.learningproject.driverapp.activity;

import com.example.learningproject.driverapp.message.Errors;

public interface HomeFragmentListener {
    public interface ShowMessageListener {
        void showErrorMessage(Errors error);
    }
}
