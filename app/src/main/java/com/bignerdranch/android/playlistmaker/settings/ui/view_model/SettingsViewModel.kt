package com.bignerdranch.android.playlistmaker.settings.ui.view_model

import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.*
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.bignerdranch.android.playlistmaker.application.App
import com.bignerdranch.android.playlistmaker.creator.Creator
import com.bignerdranch.android.playlistmaker.settings.domain.api.ISettingsInteractor
import com.bignerdranch.android.playlistmaker.settings.domain.models.ThemeSettings
import com.bignerdranch.android.playlistmaker.sharing.domain.api.ISharingInteractor

class SettingsViewModel(
    private val settingsInteractor: ISettingsInteractor,
    private val sharingInteractor: ISharingInteractor,
    private val application: App,
) : AndroidViewModel(application) {


    private var darkTheme = false
    private val themeSwitcherStateLiveData = MutableLiveData(darkTheme)

    init {
        darkTheme = settingsInteractor.getThemeSettings().darkTheme
        themeSwitcherStateLiveData.value = darkTheme
    }

    fun observeThemeSwitcherState(): LiveData<Boolean> = themeSwitcherStateLiveData

    fun onThemeSwitcherClicked(isChecked: Boolean) {
        themeSwitcherStateLiveData.value = isChecked
        settingsInteractor.updateThemeSetting(ThemeSettings(darkTheme = isChecked))
        switchTheme(isChecked)
    }

    fun onShareAppClicked() {
        sharingInteractor.shareApp()
    }

    fun onWriteSupportClicked() {
        sharingInteractor.openSupport()
    }

    fun termsOfUseClicked() {
        sharingInteractor.openTerms()
    }

    private fun switchTheme(darkThemeEnabled: Boolean) {

        darkTheme = darkThemeEnabled

        AppCompatDelegate.setDefaultNightMode(
            if (darkThemeEnabled) {
                AppCompatDelegate.MODE_NIGHT_YES
            } else {
                AppCompatDelegate.MODE_NIGHT_NO
            }
        )
    }

    companion object {
        fun getViewModelFactory(): ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = this[APPLICATION_KEY] as App
                SettingsViewModel(
                    settingsInteractor = Creator.provideSettingsInteractor(application),
                    sharingInteractor = Creator.provideSharingInteractor(application),
                    application = application,
                )
            }
        }
    }
}