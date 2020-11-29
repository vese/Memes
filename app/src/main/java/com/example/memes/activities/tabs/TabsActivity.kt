package com.example.memes.activities.tabs

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.memes.activities.tabs.fragments.AddFragment
import com.example.memes.activities.tabs.fragments.panel.PanelsFragment
import com.example.memes.activities.tabs.fragments.ProfileFragment
import com.example.memes.R
import com.google.android.material.bottomnavigation.BottomNavigationView

class TabsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tabs)
        supportFragmentManager.beginTransaction()
            .replace(R.id.mainContentFrame, PanelsFragment()).commit()

        val navigation = findViewById<BottomNavigationView>(R.id.bottomNavigationView)
        navigation.setOnNavigationItemSelectedListener {
            val selectedFragment = when (it.itemId) {
                R.id.action_panels -> PanelsFragment()
                R.id.action_add -> AddFragment()
                R.id.action_profile -> ProfileFragment()
                else -> null
            }

            if (selectedFragment != null) {
                supportFragmentManager.beginTransaction()
                    .replace(R.id.mainContentFrame, selectedFragment).commit()
            }

            true
        }
    }
}