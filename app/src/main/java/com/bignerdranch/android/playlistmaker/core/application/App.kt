package com.bignerdranch.android.playlistmaker.core.application

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import com.bignerdranch.android.playlistmaker.core.di.dataModule
import com.bignerdranch.android.playlistmaker.core.di.domainModule
import com.bignerdranch.android.playlistmaker.core.di.repositoryModule
import com.bignerdranch.android.playlistmaker.core.di.viewModelModule
import com.bignerdranch.android.playlistmaker.settings.domain.api.ISettingsInteractor
import com.markodevcic.peko.PermissionRequester
import org.koin.android.ext.android.getKoin
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level

class App : Application() {

    private var darkTheme = false

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidLogger(Level.DEBUG)
            androidContext(this@App)
            modules(dataModule, domainModule, repositoryModule, viewModelModule)
        }

        PermissionRequester.initialize(applicationContext)

        darkTheme = getKoin()
            .get<ISettingsInteractor>()
            .getThemeSettings().darkTheme

        AppCompatDelegate.setDefaultNightMode(
            if (darkTheme) {
                AppCompatDelegate.MODE_NIGHT_YES
            } else {
                AppCompatDelegate.MODE_NIGHT_NO
            }
        )
    }

    companion object {
        const val PREFERENCES = "app_preferences"
    }
}