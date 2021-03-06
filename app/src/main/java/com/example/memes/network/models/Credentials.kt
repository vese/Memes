package com.example.memes.network.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class Credentials (
    @SerializedName("login")
    @Expose
    var login: String,

    @SerializedName("password")
    @Expose
    var password: String?
)