package com.bignerdranch.android.playlistmaker.settings.data.storage.sharedprefs

import android.content.SharedPreferences
import com.bignerdranch.android.playlistmaker.settings.data.storage.models.SettingsDto

class SharedPrefsSettingsStorage(private val sharedPreferences: SharedPreferences) :
    ISettingsStorage{

    override fun getSettings(): SettingsDto {
        return SettingsDto(
            isDarkTheme = sharedPreferences.getBoolean(STATE_CHECKED_SWITCH_BUTTON, false)
        )
    }

    override fun saveSettings(settingsDto: SettingsDto) {
        sharedPreferences
            .edit()
            .putBoolean(STATE_CHECKED_SWITCH_BUTTON, settingsDto.isDarkTheme)
            .apply()
    }

    companion object {
        const val  STATE_CHECKED_SWITCH_BUTTON = "isChecked"
    }
}