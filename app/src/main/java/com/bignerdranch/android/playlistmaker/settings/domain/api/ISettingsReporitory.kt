package com.bignerdranch.android.playlistmaker.settings.domain.api

import com.bignerdranch.android.playlistmaker.settings.domain.models.ThemeSettings

interface ISettingsReporitory {
    fun getThemeSettings(): ThemeSettings
    fun updateThemeSetting(settings: ThemeSettings)
}