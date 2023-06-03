package com.bignerdranch.android.playlistmaker.di
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import com.bignerdranch.android.playlistmaker.application.App
import com.bignerdranch.android.playlistmaker.search.data.storage.SharedPrefsTracksStorage
import com.bignerdranch.android.playlistmaker.search.domain.api.TrackStorageRepository
import com.bignerdranch.android.playlistmaker.settings.data.storage.sharedprefs.ISettingsStorage
import com.bignerdranch.android.playlistmaker.settings.data.storage.sharedprefs.SharedPrefsSettingsStorage
import com.bignerdranch.android.playlistmaker.sharing.data.ExternalNavigator
import com.bignerdranch.android.playlistmaker.sharing.domain.api.IExternalNavigator
import org.koin.dsl.module

val dataModule = module {

    single {
        get<Context>().getSharedPreferences(App.PREFERENCES, AppCompatActivity.MODE_PRIVATE)
    }

    single<TrackStorageRepository> {
        SharedPrefsTracksStorage (sharedPreferences = get())
    }

    single<ISettingsStorage> {
        SharedPrefsSettingsStorage(sharedPreferences = get())
    }

    single<IExternalNavigator> {
        ExternalNavigator(get())
    }

    single<SharedPrefsTracksStorage> {
        SharedPrefsTracksStorage(sharedPreferences = get())
    }

}