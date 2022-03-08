package com.example.learningproject.uberclone.coroutines.local

import com.example.learningproject.uberclone.coroutines.parallelnetworkcall.User

class DatabaseHelperImpl (private val appDatabase: AppDatabase): DatabaseHelper {
    override suspend fun getUsers(): List<User> =appDatabase.userDao().getAll()
    override suspend fun insertAll(users: List<User>) =appDatabase.userDao().insertAll(users)
}