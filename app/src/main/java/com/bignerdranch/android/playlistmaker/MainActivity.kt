package com.bignerdranch.android.playlistmaker

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.WindowManager
import android.widget.Button
import androidx.appcompat.app.AppCompatDelegate
import com.bignerdranch.android.playlistmaker.SettingsActivity.Companion.BUTTON_SWITCH_KEY
import com.bignerdranch.android.playlistmaker.SettingsActivity.Companion.STATE_CHECKED_SWITCH_BUTTON


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN)
        setContentView(R.layout.activity_main)
        supportActionBar?.hide()
        actionBar?.hide()
        val mainMenuButtonSearch = findViewById<Button>(R.id.main_button_search)
        val mainMenuButtonMediaLibrary = findViewById<Button>(R.id.main_button_media_library)
        val mainMenuButtonSettings = findViewById<Button>(R.id.main_button_settings)

        val sharedPrefs = getSharedPreferences(STATE_CHECKED_SWITCH_BUTTON, MODE_PRIVATE)
        var switchThemeModeState = sharedPrefs.getString(BUTTON_SWITCH_KEY, "")

        when (switchThemeModeState) {
            "true" -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            "false" -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        }

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

