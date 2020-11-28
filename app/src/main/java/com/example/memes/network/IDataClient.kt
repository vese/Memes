package com.example.memes.network

import com.example.memes.network.models.MemeData
import retrofit2.Call
import retrofit2.http.GET

interface IDataClient {
    @GET("memes")
    fun getMemes(): Call<List<MemeData>?>?
}
