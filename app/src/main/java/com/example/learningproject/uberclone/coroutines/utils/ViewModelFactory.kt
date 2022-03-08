package com.example.learningproject.uberclone.coroutines.utils

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.learningproject.uberclone.coroutines.api.ApiHelper
import com.example.learningproject.uberclone.coroutines.local.DatabaseHelper
import com.example.learningproject.uberclone.coroutines.parallelnetworkcall.ParallelNetworkCallsViewModel
import com.example.learningproject.uberclone.coroutines.seriesnetworkcall.SeriesNetworkCallsViewModel

class ViewModelFactory (private val apiHelper: ApiHelper, private val dbHelper: DatabaseHelper) :
    ViewModelProvider.Factory{
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {

        if (modelClass.isAssignableFrom(SeriesNetworkCallsViewModel::class.java)) {
            return SeriesNetworkCallsViewModel(apiHelper, dbHelper) as T
        }
        if (modelClass.isAssignableFrom(ParallelNetworkCallsViewModel::class.java)) {
            return ParallelNetworkCallsViewModel(apiHelper, dbHelper) as T
        }

        throw IllegalArgumentException("Unknown class name")
    }

}