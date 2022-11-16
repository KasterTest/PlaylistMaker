package com.bignerdranch.android.playlistmaker

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        supportActionBar?.hide()
        val mainMenuButtonSearch = findViewById<Button>(R.id.main_button_search)
        val mainMenuButtonMediaLibrary = findViewById<Button>(R.id.main_button_media_library)
        val mainMenuButtonSettings = findViewById<Button>(R.id.main_button_settings)

        mainMenuButtonSearch.setOnClickListener {
            val displaySearchActivityIntent = Intent(this, SearchActivity::class.java)
            startActivity(displaySearchActivityIntent)
        }

        mainMenuButtonMediaLibrary.setOnClickListener {
            val displayMediaLibraryActivityIntent = Intent(this, MediaLibraryActivity::class.java)
            startActivity(displayMediaLibraryActivityIntent)
        }

        mainMenuButtonSettings.setOnClickListener {
            val displaySettingsActivityIntent = Intent(this, SettingsActivity::class.java)
            startActivity(displaySettingsActivityIntent)
        }

    }
}

