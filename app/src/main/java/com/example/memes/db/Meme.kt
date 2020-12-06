package com.example.memes.db

import android.database.Cursor
import com.example.memes.network.models.MemeData

class Meme {
    var id: String
    var title: String
    var description: String
    var isFavorite: Number
    var createdDate: Number
    var photoUrl: String
    var isLocal: Number = 0

    constructor(
        id: String,
        title: String,
        description: String,
        isFavorite: Boolean,
        createdDate: Number,
        photoUrl: String,
        isLocal: Boolean = false
    ) {
        this.id = id
        this.title = title
        this.description = description
        this.isFavorite = if (isFavorite) 1 else 0
        this.createdDate = createdDate
        this.photoUrl = photoUrl
        this.isLocal = if (isLocal) 1 else 0
    }

    constructor(data: MemeData) {
        this.id = data.id
        this.title = data.title
        this.description = data.description
        this.isFavorite = if (data.isFavorite) 1 else 0
        this.createdDate = data.createdDate
        this.photoUrl = data.photoUrl
    }

    val insertQuery
        get(): String = "INSERT OR IGNORE INTO $MODEL_NAME (" +
                //"$ID_COLUMN_NAME, " +
                "$TITLE_COLUMN_NAME, " +
                "$DESCRIPTION_COLUMN_NAME, " +
                "$IS_FAVORITE_COLUMN_NAME, " +
                "$CREATED_DATE_COLUMN_NAME, " +
                "$PHOTO_URL_COLUMN_NAME, " +
                "$IS_LOCAL_COLUMN_NAME) " +
                "VALUES (" +
                //"'${id}', " +
                "'${title}', " +
                "'${description}', " +
                "${isFavorite}, " +
                "${createdDate}, " +
                "'${photoUrl}', " +
                "${isLocal});"

    private val insertValues
        get(): String = "(" +
                "'${id}', " +
                "'${title}', " +
                "'${description}', " +
                "${isFavorite}, " +
                "${createdDate}, " +
                "'${photoUrl}')"

    companion object {
        private const val MODEL_NAME = "meme"
        private const val ID_COLUMN_NAME = "id"
        private const val TITLE_COLUMN_NAME = "title"
        private const val DESCRIPTION_COLUMN_NAME = "description"
        private const val IS_FAVORITE_COLUMN_NAME = "isFavorite"
        private const val CREATED_DATE_COLUMN_NAME = "createdDate"
        private const val PHOTO_URL_COLUMN_NAME = "photoUrl"
        private const val IS_LOCAL_COLUMN_NAME = "isLocal"

        val createQuery
            get() = "CREATE TABLE $MODEL_NAME (" +
                    "$ID_COLUMN_NAME INTEGER PRIMARY KEY," +
                    "$TITLE_COLUMN_NAME TEXT," +
                    "$DESCRIPTION_COLUMN_NAME TEXT," +
                    "$IS_FAVORITE_COLUMN_NAME INTEGER," +
                    "$CREATED_DATE_COLUMN_NAME INTEGER," +
                    "$PHOTO_URL_COLUMN_NAME TEXT," +
                    "$IS_LOCAL_COLUMN_NAME INTEGER DEFAULT 0" +
                    ")"

        val dropQuery get() = "DROP TABLE IF EXISTS $MODEL_NAME"

        fun getInsertQuery(memes: List<Meme>): String = memes.joinToString(
            ", ",
            "INSERT OR IGNORE INTO $MODEL_NAME (" +
                    "$ID_COLUMN_NAME, " +
                    "$TITLE_COLUMN_NAME, " +
                    "$DESCRIPTION_COLUMN_NAME, " +
                    "$IS_FAVORITE_COLUMN_NAME, " +
                    "$CREATED_DATE_COLUMN_NAME, " +
                    "$PHOTO_URL_COLUMN_NAME) " +
                    "VALUES ",
            ";"
        ) { it.insertValues }

        val deleteQuery get() = "DELETE FROM $MODEL_NAME"

        val selectQuery get() = "SELECT * FROM $MODEL_NAME ORDER BY $ID_COLUMN_NAME DESC"

        val selectLocalQuery get() = "SELECT * FROM $MODEL_NAME WHERE $IS_LOCAL_COLUMN_NAME = 1 ORDER BY $ID_COLUMN_NAME DESC"

        fun getMemeList(cursor: Cursor): ArrayList<Meme> {
            val result: ArrayList<Meme> = ArrayList()
            if (cursor.moveToFirst()) {
                result.add(getMeme(cursor))
                while (cursor.moveToNext()) {
                    result.add(getMeme(cursor))
                }
                cursor.close()
            }
            return result
        }

        fun getMeme(cursor: Cursor): Meme {
            return Meme(
                cursor.getString(cursor.getColumnIndex(ID_COLUMN_NAME)),
                cursor.getString(cursor.getColumnIndex(TITLE_COLUMN_NAME)),
                cursor.getString(cursor.getColumnIndex(DESCRIPTION_COLUMN_NAME)),
                cursor.getInt(cursor.getColumnIndex(IS_FAVORITE_COLUMN_NAME)) > 0,
                cursor.getInt(cursor.getColumnIndex(CREATED_DATE_COLUMN_NAME)),
                cursor.getString(cursor.getColumnIndex(PHOTO_URL_COLUMN_NAME)),
                cursor.getInt(cursor.getColumnIndex(IS_LOCAL_COLUMN_NAME)) > 0,
            )
        }

        fun getUpdateFavoriteQuery(id: String, isFavorite: Boolean): String =
            "UPDATE $MODEL_NAME SET $IS_FAVORITE_COLUMN_NAME = ${if (isFavorite) 1 else 0} WHERE $ID_COLUMN_NAME = '${id}'"
    }
}