package com.example.learningproject.uberclone.coroutines.api

import com.example.learningproject.uberclone.coroutines.model.ApiUser

interface ApiHelper {
    suspend fun getUsers():List<ApiUser>
    suspend fun getMoreUsers():List<ApiUser>
    suspend fun getUsersWithError():List<ApiUser>
}