package com.example.memes.network.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class UserInfo(
    @SerializedName("id")
    @Expose
    var id: Int,

    @SerializedName("username")
    @Expose
    var username: String,

    @SerializedName("firstName")
    @Expose
    var firstName: String,

    @SerializedName("lastName")
    @Expose
    var lastName: String,

    @SerializedName("userDescription")
    @Expose
    var userDescription: String
)