package com.example.learningproject.uberclone.di

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.learningproject.R
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
import javax.inject.Named


@AndroidEntryPoint
class DependencyInjection : AppCompatActivity() {
    @Inject
    @Named("String1")
    lateinit var testString:String
    private val viewModel:MainViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dependency_injection)
        Log.d("Dependency Injection","Test String:$testString")
        viewModel
    }
}