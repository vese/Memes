package com.example.memes.activities.splash

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import com.example.memes.R
import com.example.memes.activities.auth.AuthActivity

class SplashActivity : AppCompatActivity() {
    private val handler: Handler = Handler()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        handler.postDelayed(
            {
                startActivity(Intent(this, AuthActivity::class.java))
            }, this.resources.getInteger(
                R.integer.splashDelay
            ).toLong()
        )
    }
}