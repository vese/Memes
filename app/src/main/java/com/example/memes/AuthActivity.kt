package com.example.memes

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import studio.carbonylgroup.textfieldboxes.TextFieldBoxes

class AuthActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_auth)

        val spinner = findViewById<ProgressBar>(R.id.progress_bar)
        spinner.visibility = View.VISIBLE


        val passwordInput = findViewById<TextFieldBoxes>(R.id.password_input)
        passwordInput.helperText = getString(R.string.password_hint)
        passwordInput.setError(getString(R.string.password_hint), true)
    }
}