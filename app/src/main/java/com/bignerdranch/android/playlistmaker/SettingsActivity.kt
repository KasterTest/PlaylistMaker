package com.bignerdranch.android.playlistmaker

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class SettingsActivity : AppCompatActivity() {
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)
        supportActionBar?.hide()
        val settingsButtonShare= findViewById<Button>(R.id.button_share)
        val settingsButtonSupport = findViewById<Button>(R.id.button_support)
        val settingsButtonAgreement= findViewById<Button>(R.id.button_agreement)

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

