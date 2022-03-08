package com.example.learningproject.uberclone.coroutines.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.learningproject.uberclone.coroutines.parallelnetworkcall.User


@Database(entities = [User::class], version = 1)
abstract class AppDatabase :RoomDatabase(){
    abstract fun userDao():UserDao

}
