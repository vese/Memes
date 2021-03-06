package com.example.memes.network.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class AuthResult(
    @SerializedName("accessToken")
    @Expose
    var accessToken: String,

    @SerializedName("userInfo")
    @Expose
    var userInfo: UserInfo
)