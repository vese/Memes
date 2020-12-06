package com.example.memes.network.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class ErrorResult(
    @SerializedName("code")
    @Expose
    var code: String,

    @SerializedName("errorMessage")
    @Expose
    var errorMessage: String
)