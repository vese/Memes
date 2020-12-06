package com.example.memes.network.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class MemeData(
    @SerializedName("id")
    @Expose
    var id: String,

    @SerializedName("title")
    @Expose
    var title: String,

    @SerializedName("description")
    @Expose
    var description: String,

    @SerializedName("isFavorite")
    @Expose
    var isFavorite: Boolean,

    @SerializedName("createdDate")
    @Expose
    var createdDate: Number,

    @SerializedName("photoUrl")
    @Expose
    var photoUrl: String
)