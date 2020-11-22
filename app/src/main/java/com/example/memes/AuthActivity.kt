package com.example.memes

import android.graphics.Point
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
import android.text.method.PasswordTransformationMethod
import android.view.MotionEvent
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.ProgressBar
import android.widget.TextView
import studio.carbonylgroup.textfieldboxes.ExtendedEditText
import studio.carbonylgroup.textfieldboxes.TextFieldBoxes
import kotlin.math.roundToInt

class AuthActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_auth)

        val passwordInputText = findViewById<ExtendedEditText>(R.id.passwordInputText)
        passwordInputText.inputType = InputType.TYPE_TEXT_VARIATION_PASSWORD
        passwordInputText.transformationMethod = PasswordTransformationMethod.getInstance()
        val passwordInput = findViewById<TextFieldBoxes>(R.id.passwordInput)
        passwordInputText.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable) {
                if (s.length < 6) {
                    passwordInput.helperText = getString(R.string.password_hint)
                } else {
                    passwordInput.helperText = " "
                }
            }

            override fun beforeTextChanged(s: CharSequence, start: Int,
                                           count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence, start: Int,
                                       before: Int, count: Int) {
            }
        })
        passwordInputText.onFocusChangeListener = View.OnFocusChangeListener { _, hasFocus ->
            run {
                if (!hasFocus && passwordInputText.text.isEmpty()) {
                    passwordInput.setError(getString(R.string.empty_field_error), false)
                }
            }
        }

        val loginInputText = findViewById<ExtendedEditText>(R.id.loginInputText)
        val loginInput = findViewById<TextFieldBoxes>(R.id.loginInput)
        loginInputText.onFocusChangeListener = View.OnFocusChangeListener { _, hasFocus ->
            run {
                if (!hasFocus && loginInputText.text.isEmpty()) {
                    loginInput.setError(getString(R.string.empty_field_error), false)
                }
            }
        }
    }

    fun changeVisibility(view: View) {
        val passwordVisibilityButton = findViewById<ImageButton>(R.id.passwordVisibilityButton)
        val passwordInputText = findViewById<ExtendedEditText>(R.id.passwordInputText)
        if (passwordInputText.inputType == InputType.TYPE_TEXT_VARIATION_PASSWORD) {
            passwordVisibilityButton.setImageResource(R.drawable.eye)
            passwordInputText.inputType = InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
            passwordInputText.transformationMethod = null
            passwordInputText.setSelection(passwordInputText.text.length)
        } else {
            passwordVisibilityButton.setImageResource(R.drawable.eye_hidden)
            passwordInputText.inputType = InputType.TYPE_TEXT_VARIATION_PASSWORD
            passwordInputText.transformationMethod = PasswordTransformationMethod.getInstance()
            passwordInputText.setSelection(passwordInputText.text.length)
        }
    }

    fun login(view: View) {
        val loginInputText = findViewById<ExtendedEditText>(R.id.loginInputText)
        val passwordInputText = findViewById<ExtendedEditText>(R.id.passwordInputText)
        if (loginInputText.text.isEmpty() || passwordInputText.text.length < 6) {
            return
        }

        val spinner = findViewById<ProgressBar>(R.id.progressBar)
        val button = findViewById<Button>(R.id.button)
        spinner.visibility = View.VISIBLE
        button.text = ""
        val handler = Handler()
        handler.postDelayed({
            spinner.visibility = View.INVISIBLE
            button.text = getString(R.string.log_in)

            val errorTextView = findViewById<View>(R.id.view)
            val textView = findViewById<TextView>(R.id.textView)
            errorTextView.visibility = View.VISIBLE
            textView.visibility = View.VISIBLE
        }, 1000)
    }

    override fun dispatchTouchEvent(ev: MotionEvent): Boolean {
        val errorTextView = findViewById<View>(R.id.view)
        if (errorTextView.y > ev.rawY && errorTextView.visibility != View.GONE) {
            val textView = findViewById<TextView>(R.id.textView)
            errorTextView.visibility = View.GONE
            textView.visibility = View.GONE
        }

        return super.dispatchTouchEvent(ev)
    }

    fun hideError(view: View) {
        val errorTextView = findViewById<View>(R.id.view)
        if (errorTextView.visibility != View.GONE) {
            val textView = findViewById<TextView>(R.id.textView)
            errorTextView.visibility = View.GONE
            textView.visibility = View.GONE
        }
    }
}