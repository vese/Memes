package com.example.memes.activities.tabs.fragments.panel

import com.example.memes.db.Meme
import com.example.memes.network.models.MemeData

class Panel(
    var id: String,
    var photoUrl: String,
    var title: String,
    var isFavorite: Boolean,
    var description: String,
    var createdDate: Number
) {

    constructor(meme: MemeData) : this(
        meme.id!!,
        meme.photoUrl!!,
        meme.title!!,
        meme.isFavorite!!,
        meme.description!!,
        meme.createdDate!!
    ) {
    }

    constructor(meme: Meme) : this(
        meme.id,
        meme.photoUrl,
        meme.title,
        meme.isFavorite != 0,
        meme.description,
        meme.createdDate
    )
}