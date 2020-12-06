package com.example.memes.activities.tabs

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.MotionEvent
import com.example.memes.activities.tabs.fragments.panel.PanelsFragment
import com.example.memes.activities.tabs.fragments.profile.ProfileFragment
import com.example.memes.R
import com.example.memes.activities.add.AddActivity
import com.google.android.material.bottomnavigation.BottomNavigationView

class TabsActivity : AppCompatActivity() {

    private lateinit var navigation: BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tabs)

        navigation = findViewById(R.id.bottomNavigationView)

        supportFragmentManager.beginTransaction()
            .replace(R.id.mainContentFrame, PanelsFragment()).commit()

        navigation.setOnNavigationItemSelectedListener {
            onNavigationItemSelected(it)
        }
    }

    private fun onNavigationItemSelected(it: MenuItem): Boolean {
        if (it.itemId == R.id.actionAdd) {
            startActivity(Intent(this, AddActivity::class.java))
            return false
        }

        val selectedFragment = when (it.itemId) {
            R.id.actionPanels -> PanelsFragment()
            R.id.actionProfile -> ProfileFragment()
            else -> null
        }

        if (selectedFragment != null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.mainContentFrame, selectedFragment).commit()
        }

        return true
    }

    override fun onResume() {
        super.onResume()
        val fragments = supportFragmentManager.fragments
        val panelsFragment = fragments.find { it is PanelsFragment } as PanelsFragment?
        panelsFragment?.initialLoad()
        val profileFragment = fragments.find { it is ProfileFragment } as ProfileFragment?
        profileFragment?.initialLoad()
    }

    override fun dispatchTouchEvent(ev: MotionEvent): Boolean {
        val fragments = supportFragmentManager.fragments
        val profileFragment = fragments.find { it is ProfileFragment } as ProfileFragment?
        profileFragment?.dispatchTouchEvent(ev)
        return super.dispatchTouchEvent(ev)
    }
}