package com.example.learningproject.adavancedkotlin

import retrofit2.http.GET

interface ApiService {
    @GET("users")
    suspend fun getUsers(): List<ApiUser>
}