package com.example.learningproject.uberclone.coroutines.seriesnetworkcall

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.learningproject.uberclone.coroutines.api.ApiHelper
import com.example.learningproject.uberclone.coroutines.local.DatabaseHelper
import com.example.learningproject.uberclone.coroutines.model.ApiUser
import com.example.learningproject.uberclone.coroutines.utils.Resource
import kotlinx.coroutines.launch

class SeriesNetworkCallsViewModel(private val apiHelper: ApiHelper, private val dbHelper: DatabaseHelper) :ViewModel(){
    private val users = MutableLiveData<Resource<List<ApiUser>>>()

    init {
        fetchUsers()
    }

    private fun fetchUsers() {
        viewModelScope.launch {
            users.postValue(Resource.loading(null))
            try {
                val usersFromApi = apiHelper.getUsers()
                val moreUsersFromApi = apiHelper.getMoreUsers()
                val allUsersFromApi = mutableListOf<ApiUser>()
                allUsersFromApi.addAll(usersFromApi)
                allUsersFromApi.addAll(moreUsersFromApi)
                users.postValue(Resource.success(allUsersFromApi))
            } catch (e: Exception) {
                users.postValue(Resource.error("Something Went Wrong", null))
            }
        }
    }

    fun getUsers(): LiveData<Resource<List<ApiUser>>> {
        return users
    }
}