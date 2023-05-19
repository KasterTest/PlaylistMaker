package com.bignerdranch.android.playlistmaker.settings.data.storage.sharedprefs

import com.bignerdranch.android.playlistmaker.settings.data.storage.models.SettingsDto

interface ISettingsStorage {
    fun getSettings(): SettingsDto
    fun saveSettings(settingsDto: SettingsDto)
}