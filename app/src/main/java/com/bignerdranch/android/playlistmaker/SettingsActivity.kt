package com.bignerdranch.android.playlistmaker

import android.annotation.SuppressLint
import android.content.Intent
import android.content.res.Configuration
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.Switch
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_YES


const val STATE_CHECKED_SWITCH_BUTTON = "isChecked"
const val BUTTON_SWITCH_KEY = "key_for_switch_button"

class SettingsActivity : AppCompatActivity() {
    @SuppressLint("UseSwitchCompatOrMaterialCode")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)
        supportActionBar?.hide()
        val settingsButtonShare= findViewById<Button>(R.id.button_share)
        val settingsButtonSupport = findViewById<Button>(R.id.button_support)
        val settingsButtonAgreement= findViewById<Button>(R.id.button_agreement)

        val switchThemeMode = findViewById<Switch>(R.id.switch_theme)

        // Использует по умолчанию темную тему, если она испоьзуется в системе
        when (resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK) {
            Configuration.UI_MODE_NIGHT_YES -> {switchThemeMode.isChecked = true}
       }

        val sharedPrefs = getSharedPreferences(STATE_CHECKED_SWITCH_BUTTON, MODE_PRIVATE)

        switchThemeMode.setOnCheckedChangeListener { _, isChecked ->
            sharedPrefs.edit()
                .putString(BUTTON_SWITCH_KEY, switchThemeMode.isChecked.toString())
                .apply()
            if (isChecked) {
                AppCompatDelegate.setDefaultNightMode(MODE_NIGHT_YES)
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
        }

        settingsButtonSupport.setOnClickListener{
            val writeToSupportIntent = Intent(Intent.ACTION_SENDTO).apply {
                data = Uri.parse("mailto:")
                putExtra(Intent.EXTRA_EMAIL, arrayOf(getString(R.string.settings_support_email)))
                putExtra(Intent.EXTRA_SUBJECT, getString(R.string.settings_support_subject_message))
                putExtra(Intent.EXTRA_TEXT, getString(R.string.setting_supports_text_message))
            }
            startActivity(writeToSupportIntent)
        }
        settingsButtonAgreement.setOnClickListener{
            val openYPAgreementIntent = Intent(Intent.ACTION_VIEW)
            val yandexPracticumAgreement = getString(R.string.settings_agreement_link)
            openYPAgreementIntent.data = Uri.parse(yandexPracticumAgreement)
            startActivity(openYPAgreementIntent)
        }
        settingsButtonShare.setOnClickListener {
            val shareAppIntent = Intent(Intent.ACTION_SEND)
            shareAppIntent.type = "text/plain"
            shareAppIntent.putExtra(Intent.EXTRA_TEXT, getString(R.string.settings_share_text))
            startActivity(shareAppIntent)
        }

    }
}
