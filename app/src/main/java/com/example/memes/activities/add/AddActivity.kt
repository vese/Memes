package com.example.memes.activities.add

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.example.memes.R
import com.example.memes.activities.add.fragments.ImageLoadDialogFragment
import com.example.memes.db.DBHelper
import com.example.memes.db.Meme
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.textfield.TextInputLayout
import java.util.*

class AddActivity : AppCompatActivity() {

    private lateinit var titleText: EditText
    private lateinit var titleTextLayout: TextInputLayout
    private lateinit var descriptionText: EditText
    private lateinit var descriptionTextLayout: TextInputLayout
    private lateinit var imageView: ImageView
    private lateinit var resetImageButton: ImageButton
    private lateinit var openLoadImageDialogButton: FloatingActionButton
    private lateinit var createButton: Button
    private lateinit var photoUrl: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add)

        titleText = findViewById(R.id.titleText)
        titleTextLayout = findViewById(R.id.titleTextLayout)
        descriptionText = findViewById(R.id.descriptionText)
        descriptionTextLayout = findViewById(R.id.descriptionTextLayout)
        imageView = findViewById(R.id.loadedImageView)
        resetImageButton = findViewById(R.id.resetImageButton)
        openLoadImageDialogButton = findViewById(R.id.openLoadImageDialogButton)
        createButton = findViewById(R.id.createButton)

        titleText.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable) {
                checkFilled()
                titleTextLayout.isCounterEnabled = s.length > 140
            }

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
            }
        })
        descriptionText.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable) {
                checkFilled()
                descriptionTextLayout.isCounterEnabled = s.length > 1000
            }

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
            }
        })
    }

    fun returnBack(view: View) {
        finish()
    }

    fun openLoadImageDialog(view: View) {
        val dialog = ImageLoadDialogFragment()
        dialog.show(supportFragmentManager, "fragment_image_load_dialog")
        supportFragmentManager.executePendingTransactions()
        dialog.setOnImageChosenListener { photoUrl -> loadImage(photoUrl) }
    }

    private fun loadImage(photoUrl: String) {
        this.photoUrl = photoUrl
        imageView.visibility = View.VISIBLE
        Glide.with(this).load(photoUrl).into(imageView)
        resetImageButton.visibility = View.VISIBLE
        checkFilled()
        openLoadImageDialogButton.isEnabled = false
    }

    fun resetImage(view: View) {
        imageView.visibility = View.GONE
        view.visibility = View.GONE
        checkFilled()
        openLoadImageDialogButton.isEnabled = true
    }

    private fun checkFilled() {
        val titleTextLength = titleText.length()
        val descriptionTextLength = descriptionText.length()

        createButton.isEnabled = imageView.visibility != View.GONE &&
                titleTextLength > 0 && titleTextLength <= 140 &&
                descriptionTextLength > 0 && descriptionTextLength <= 1000
    }

    fun addToDb(view: View) {
        val dbHelper = DBHelper(this)
        dbHelper.insertMeme(
            Meme(
                "",
                titleText.text.toString(),
                descriptionText.text.toString(),
                false,
                Calendar.getInstance().timeInMillis / 1000,
                photoUrl,
                true
            )
        )
        finish()
    }
}