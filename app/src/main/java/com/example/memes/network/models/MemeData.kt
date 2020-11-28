package com.example.memes.network.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class MemeData {
    @SerializedName("id")
    @Expose
    var id: String? = null

    @SerializedName("title")
    @Expose
    var title: String? = null

    @SerializedName("description")
    @Expose
    var description: String? = null

    @SerializedName("isFavorite")
    @Expose
    var isFavorite: Boolean? = null

    @SerializedName("createdDate")
    @Expose
    var createdDate: Number? = null

    @SerializedName("photoUrl")
    @Expose
    var photoUrl: String? = null
}