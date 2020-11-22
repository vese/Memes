package com.example.memes.repository

import android.content.Context
import android.content.SharedPreferences
import com.example.memes.repository.Consts.USER_ACCESS_TOKEN
import com.example.memes.repository.Consts.USER_DESCRIPTION
import com.example.memes.repository.Consts.USER_FIRST_NAME
import com.example.memes.repository.Consts.USER_ID
import com.example.memes.repository.Consts.USER_INFO_KEY
import com.example.memes.repository.Consts.USER_LAST_NAME
import com.example.memes.repository.Consts.USER_USERNAME
import com.example.memes.repository.models.User
import com.example.memes.network.models.AuthResult


class UserRepository(private val context: Context) {
    fun saveUser(data: AuthResult) {
        val userSharedPreferences: SharedPreferences =
            context.getSharedPreferences(USER_INFO_KEY, Context.MODE_PRIVATE)
        val editor: SharedPreferences.Editor = userSharedPreferences.edit()
        editor.putString(USER_ACCESS_TOKEN, data.accessToken)
        editor.putInt(USER_ID, data.userInfo!!.id)
        editor.putString(USER_USERNAME, data.userInfo!!.username)
        editor.putString(USER_FIRST_NAME, data.userInfo!!.firstName)
        editor.putString(USER_LAST_NAME, data.userInfo!!.lastName)
        editor.putString(USER_DESCRIPTION, data.userInfo!!.userDescription)
        editor.apply()
    }

    fun getUser(): User? {
        val userSharedPreferences: SharedPreferences =
            context.getSharedPreferences(USER_INFO_KEY, Context.MODE_PRIVATE)
        if (!userSharedPreferences.contains(USER_ACCESS_TOKEN)) {
            return null
        }

        val user = User()
        user.accessToken = userSharedPreferences.getString(USER_ACCESS_TOKEN, "")
        user.id = userSharedPreferences.getInt(USER_ID, 0)
        user.username = userSharedPreferences.getString(USER_USERNAME, "")
        user.firstName = userSharedPreferences.getString(USER_FIRST_NAME, "")
        user.lastName = userSharedPreferences.getString(USER_LAST_NAME, "")
        user.userDescription = userSharedPreferences.getString(USER_DESCRIPTION, "")

        return user
    }
}