package com.example.learningproject.rxjava.utils

import com.example.learningproject.rxjava.model.ApiUser
import com.example.learningproject.rxjava.model.User

object Utils {
    fun getApiuserList():List<ApiUser>
    {
        val apiUserList=ArrayList<ApiUser>()
        val apiUserOne=ApiUser(firstname = "Priya",lastname = "Dharshini")
        apiUserList.add(apiUserOne)
        val apiUserTwo = ApiUser(firstname = "Ragu", lastname = "Nath")
        apiUserList.add(apiUserTwo)

        val apiUserThree = ApiUser(firstname = "Hitesh", lastname = "Abinav")
        apiUserList.add(apiUserThree)

        return apiUserList
    }
    fun getUserListWhoLovesCricket(): List<User> {

        val userList = java.util.ArrayList<User>()

        val userOne = User(id = 1, firstname = "Priya", lastname = "Dharshini")
        userList.add(userOne)

        val userTwo = User(id = 2, firstname = "Ragu", lastname = "Nath")
        userList.add(userTwo)

        return userList
    }
    fun getUserListWhoLovesFootball(): List<User> {

        val userList = java.util.ArrayList<User>()

        val userOne = User(id = 1, firstname = "Priya", lastname = "Dharshini")
        userList.add(userOne)

        val userTwo = User(id = 3, firstname = "Ragu", lastname = "Nath")
        userList.add(userTwo)

        return userList
    }
    fun convertApiUserListToUserList(apiUserList: List<ApiUser>): List<User> {

        val userList = java.util.ArrayList<User>()

        for (apiUser in apiUserList) {
            val user = User(apiUser.id, apiUser.firstname, apiUser.lastname)
            userList.add(user)
        }

        return userList
    }

    fun filterUserWhoLovesBoth(cricketFans: List<User>, footballFans: List<User>): List<User> {
        val userWhoLovesBoth = java.util.ArrayList<User>()

        for (footballFan in footballFans) {
            if (cricketFans.contains(footballFan)) {
                userWhoLovesBoth.add(footballFan)
            }
        }

        return userWhoLovesBoth
    }


}