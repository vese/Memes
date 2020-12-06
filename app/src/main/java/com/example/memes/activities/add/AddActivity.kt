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

    private var photoUrl: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add)

        val titleText = findViewById<EditText>(R.id.titleText)
        val titleTextLayout = findViewById<TextInputLayout>(R.id.titleTextLayout)
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
        val descriptionText = findViewById<EditText>(R.id.descriptionText)
        val descriptionTextLayout = findViewById<TextInputLayout>(R.id.descriptionTextLayout)
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
        val imageView = findViewById<ImageView>(R.id.loadedImageView)
        imageView.visibility = View.VISIBLE
        Glide.with(this).load(photoUrl).into(imageView)
        val resetImageButton = findViewById<ImageButton>(R.id.resetImageButton)
        resetImageButton.visibility = View.VISIBLE
        checkFilled()
        val openLoadImageDialogButton = findViewById<FloatingActionButton>(R.id.openLoadImageDialogButton)
        openLoadImageDialogButton.isEnabled = false
    }

    fun resetImage(view: View) {
        val imageView = findViewById<ImageView>(R.id.loadedImageView)
        imageView.visibility = View.GONE
        view.visibility = View.GONE
        checkFilled()
        val openLoadImageDialogButton = findViewById<FloatingActionButton>(R.id.openLoadImageDialogButton)
        openLoadImageDialogButton.isEnabled = true
    }

    private fun checkFilled() {
        val imageView = findViewById<ImageView>(R.id.loadedImageView)
        val titleText = findViewById<EditText>(R.id.titleText)
        val titleTextLength = titleText.length()
        val descriptionText = findViewById<EditText>(R.id.descriptionText)
        val descriptionTextLength = descriptionText.length()

        val createButton = findViewById<Button>(R.id.createButton)
        createButton.isEnabled = imageView.visibility != View.GONE &&
                titleTextLength > 0 && titleTextLength <= 140 &&
                descriptionTextLength > 0 && descriptionTextLength <= 1000
    }

    fun addToDb(view: View) {
        val titleText = findViewById<EditText>(R.id.titleText)
        val descriptionText = findViewById<EditText>(R.id.descriptionText)
        val dbHelper = DBHelper(this)
        photoUrl?.let {
            dbHelper.insertMeme(
                Meme(
                    "",
                    titleText.text.toString(),
                    descriptionText.text.toString(),
                    false,
                    Calendar.getInstance().timeInMillis / 1000,
                    it,
                    true
                )
            )
        }
        finish()
    }
}