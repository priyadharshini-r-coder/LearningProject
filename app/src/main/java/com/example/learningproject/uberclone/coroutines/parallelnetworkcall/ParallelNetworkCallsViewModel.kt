package com.example.learningproject.uberclone.coroutines.parallelnetworkcall

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope


import com.example.learningproject.uberclone.coroutines.api.ApiHelper
import com.example.learningproject.uberclone.coroutines.local.DatabaseHelper
import com.example.learningproject.uberclone.coroutines.model.ApiUser
import com.example.learningproject.uberclone.coroutines.utils.Resource
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch

class ParallelNetworkCallsViewModel(private val apiHelper: ApiHelper,private val dbHelper: DatabaseHelper) : ViewModel(){
    private val users = MutableLiveData<Resource<List<ApiUser>>>()

    init {
        fetchUsers()
    }

    private fun fetchUsers() {

        viewModelScope.launch {
            users.postValue(Resource.loading(null))
            try {
                // coroutineScope is needed, else in case of any network error, it will crash
                coroutineScope {
                    val usersFromApiDeferred = async { apiHelper.getUsers() }
                    val moreUsersFromApiDeferred = async { apiHelper.getMoreUsers() }

                    val usersFromApi = usersFromApiDeferred.await()
                    val moreUsersFromApi = moreUsersFromApiDeferred.await()

                    val allUsersFromApi = mutableListOf<ApiUser>()
                    allUsersFromApi.addAll(usersFromApi)
                    allUsersFromApi.addAll(moreUsersFromApi)

                    users.postValue(Resource.success(allUsersFromApi))
                }
            } catch (e: Exception) {
                users.postValue(Resource.error("Something Went Wrong", null))
            }
        }
    }

    fun getUsers(): LiveData<Resource<List<ApiUser>>> {
        return users
    }
}