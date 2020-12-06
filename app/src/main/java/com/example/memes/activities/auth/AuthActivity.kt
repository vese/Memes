package com.example.memes.activities.auth

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
import android.text.method.PasswordTransformationMethod
import android.view.KeyEvent
import android.view.MotionEvent
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.memes.R
import com.example.memes.activities.tabs.TabsActivity
import com.example.memes.network.NetworkService
import com.example.memes.network.models.AuthResult
import com.example.memes.network.models.Credentials
import com.example.memes.network.models.ErrorResult
import com.example.memes.repository.UserRepository
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import studio.carbonylgroup.textfieldboxes.ExtendedEditText
import studio.carbonylgroup.textfieldboxes.TextFieldBoxes

class AuthActivity : AppCompatActivity() {

    private lateinit var passwordInputText: ExtendedEditText
    private lateinit var passwordInput: TextFieldBoxes
    private lateinit var loginInputText: ExtendedEditText
    private lateinit var loginInput: TextFieldBoxes
    private lateinit var passwordVisibilityButton: ImageButton
    private lateinit var spinner: ProgressBar
    private lateinit var button: Button
    private lateinit var errorView: View
    private lateinit var errorTextView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_auth)

        passwordInputText = findViewById(R.id.passwordInputText)
        passwordInput = findViewById(R.id.passwordInput)
        loginInputText = findViewById(R.id.loginInputText)
        loginInput = findViewById(R.id.loginInput)
        passwordVisibilityButton = findViewById(R.id.passwordVisibilityButton)
        spinner = findViewById(R.id.progressBar)
        button = findViewById(R.id.button)
        errorView = findViewById(R.id.errorView)
        errorTextView = findViewById(R.id.errorTextView)

        passwordInputText.inputType = InputType.TYPE_TEXT_VARIATION_PASSWORD
        passwordInputText.transformationMethod = PasswordTransformationMethod.getInstance()
        passwordInputText.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable) {
                if (s.length < 6) {
                    passwordInput.helperText = getString(R.string.password_hint)
                } else {
                    passwordInput.helperText = " "
                }
            }

            override fun beforeTextChanged(
                s: CharSequence, start: Int,
                count: Int, after: Int
            ) {
            }

            override fun onTextChanged(
                s: CharSequence, start: Int,
                before: Int, count: Int
            ) {
            }
        })

        passwordInputText.onFocusChangeListener = View.OnFocusChangeListener { _, hasFocus ->
            run {
                if (!hasFocus && passwordInputText.text.isEmpty()) {
                    passwordInput.setError(getString(R.string.empty_field_error), false)
                }
            }
        }

        loginInputText.onFocusChangeListener = View.OnFocusChangeListener { _, hasFocus ->
            run {
                if (!hasFocus && loginInputText.text.isEmpty()) {
                    loginInput.setError(getString(R.string.empty_field_error), false)
                }
            }
        }
    }

    fun changeVisibility(view: View) {
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
        if (loginInputText.text.isEmpty() || passwordInputText.text.length < 6) {
            return
        }

        spinner.visibility = View.VISIBLE
        button.text = ""
        val handler = Handler()
        val credentials = Credentials(
            "qwerty", //loginInputText.text.toString(),
            "qwerty" //passwordInputText.text.toString()
        )
        handler.postDelayed({
            NetworkService.authClient.login(credentials).enqueue(object : Callback<AuthResult> {
                override fun onResponse(
                    call: Call<AuthResult>,
                    response: Response<AuthResult>
                ) {
                    if (response.isSuccessful) {
                        val result = response.body()
                        if (result != null) {
                            val repository = UserRepository(applicationContext)
                            repository.saveUser(result)
                            startActivity(Intent(applicationContext, TabsActivity::class.java))
                        }
                    } else {
                        val errorResponse: ErrorResult? = Gson().fromJson(
                            response.errorBody()?.charStream(),
                            object : TypeToken<ErrorResult>() {}.type
                        )
                        errorView.visibility = View.VISIBLE
                        errorTextView.visibility = View.VISIBLE
                    }
                    spinner.visibility = View.GONE
                    button.text = getString(R.string.log_in)
                }

                override fun onFailure(call: Call<AuthResult>, t: Throwable) {
                }
            })
        }, this.resources.getInteger(R.integer.delay).toLong())
    }

    override fun dispatchTouchEvent(ev: MotionEvent): Boolean {
        if (errorView.y > ev.rawY && errorView.visibility != View.GONE) {
            errorView.visibility = View.GONE
            errorTextView.visibility = View.GONE
        }

        return super.dispatchTouchEvent(ev)
    }

    fun hideError(view: View) {
        if (errorView.visibility != View.GONE) {
            errorView.visibility = View.GONE
            errorTextView.visibility = View.GONE
        }
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            finishAffinity()
        }
        return super.onKeyDown(keyCode, event)
    }
}