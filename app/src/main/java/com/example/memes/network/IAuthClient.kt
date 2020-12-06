package com.example.memes.network

import com.example.memes.network.models.AuthResult
import com.example.memes.network.models.Credentials
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface IAuthClient {
    @POST("auth/login")
    fun login(@Body data: Credentials): Call<AuthResult>

    @POST("auth/logout")
    fun logout(): Call<Void>
}