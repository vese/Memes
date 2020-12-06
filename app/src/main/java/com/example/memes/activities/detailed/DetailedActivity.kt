package com.example.memes.activities.detailed

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.example.memes.R
import com.example.memes.activities.tabs.fragments.panel.Panel
import com.example.memes.db.DBHelper
import com.google.gson.Gson
import java.util.*

class DetailedActivity : AppCompatActivity() {

    private lateinit var dbHelper: DBHelper
    private lateinit var panel: Panel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detailed)

        dbHelper = DBHelper(this)
        panel = Gson().fromJson(intent.getStringExtra("panel"), Panel::class.java)

        val title = findViewById<TextView>(R.id.title)
        title.text = panel.title
        val description = findViewById<TextView>(R.id.description)
        description.text = panel.description
        val memeImage = findViewById<ImageView>(R.id.memeImage)
        Glide.with(this).load(panel.photoUrl).into(memeImage)
        val isFavoriteButton = findViewById<ImageButton>(R.id.isFavoriteButton2)
        isFavoriteButton.isSelected = panel.isFavorite
        val createdDateText = findViewById<TextView>(R.id.createdDateText)
        val days =
            (Calendar.getInstance().timeInMillis / 1000 - panel.createdDate.toLong()) / 60 / 60 / 24
        createdDateText.text = getString(R.string.days_ago, days)
    }

    fun favoriteClickListener(view: View) {
        panel.isFavorite = !panel.isFavorite
        view.isSelected = panel.isFavorite
        dbHelper.updateFavorite(panel.id, panel.isFavorite)
    }

    fun returnBack(view: View) {
        finish()
    }
}