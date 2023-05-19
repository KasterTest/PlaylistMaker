package com.bignerdranch.android.playlistmaker.settings.domain.impl


import com.bignerdranch.android.playlistmaker.settings.domain.api.ISettingsInteractor
import com.bignerdranch.android.playlistmaker.settings.domain.api.ISettingsReporitory
import com.bignerdranch.android.playlistmaker.settings.domain.models.ThemeSettings

class SettingsInteractor(private val repository: ISettingsReporitory): ISettingsInteractor {
    override fun getThemeSettings() : ThemeSettings {
        return repository.getThemeSettings()
    }

    override fun updateThemeSetting(settings: ThemeSettings) {
        repository.updateThemeSetting(settings)
    }


}