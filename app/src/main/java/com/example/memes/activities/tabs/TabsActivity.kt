package com.example.memes.activities.tabs

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.memes.activities.tabs.fragments.panel.PanelsFragment
import com.example.memes.activities.tabs.fragments.ProfileFragment
import com.example.memes.R
import com.example.memes.activities.add.AddActivity
import com.google.android.material.bottomnavigation.BottomNavigationView

class TabsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tabs)
        supportFragmentManager.beginTransaction()
            .replace(R.id.mainContentFrame, PanelsFragment()).commit()

        val navigation = findViewById<BottomNavigationView>(R.id.bottomNavigationView)
        navigation.setOnNavigationItemSelectedListener {
            if (it.itemId == R.id.action_add) {
                startActivity(Intent(this, AddActivity::class.java))
                return@setOnNavigationItemSelectedListener false
            }

            val selectedFragment = when (it.itemId) {
                R.id.action_panels -> PanelsFragment()
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

    override fun onResume() {
        super.onResume()
        val fragments = supportFragmentManager.fragments
        val panelsFragment: PanelsFragment? = fragments.find { it is PanelsFragment } as PanelsFragment?
        panelsFragment?.initialLoad()
    }
}