package com.example.learningproject.uberclone.di

import android.util.Log
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import javax.inject.Named

class MainViewModel @ViewModelInject constructor(@Named("String1") testString: String):ViewModel(){
    init{
        Log.d("ViewModel","Test String from ViewModel:$testString")
    }

}