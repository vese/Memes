package com.example.memes.db

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DBHelper(context: Context, factory: SQLiteDatabase.CursorFactory?) :
    SQLiteOpenHelper(context, DATABASE_NAME, factory, DATABASE_VERSION) {

    constructor(context: Context) : this(context, null)

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(Meme.createQuery)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL(Meme.dropQuery)
        onCreate(db)
    }

    override fun onDowngrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL(Meme.dropQuery)
        onCreate(db)
    }

    fun deleteMemes() {
        val db = this.readableDatabase
        db.execSQL(Meme.deleteQuery)
    }

    fun insertMemes(memes: List<Meme>) {
        deleteMemes()
        val db = this.readableDatabase
        db.execSQL(Meme.getInsertQuery(memes))
    }

    fun getMemeList(): ArrayList<Meme> {
        val db = this.readableDatabase
        val cursor = db.rawQuery(Meme.selectQuery, null)
        val result = Meme.getMemeList(cursor)
        cursor.close()
        return result
    }

    companion object {
        private const val DATABASE_VERSION = 1
        private const val DATABASE_NAME = "healthDiary.db"
    }
}