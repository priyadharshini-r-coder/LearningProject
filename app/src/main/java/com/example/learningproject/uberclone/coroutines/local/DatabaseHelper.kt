package com.example.learningproject.uberclone.coroutines.local

import com.example.learningproject.uberclone.coroutines.parallelnetworkcall.User


interface DatabaseHelper {
    suspend fun getUsers(): List<User>

    suspend fun insertAll(users: List<User>)
}
