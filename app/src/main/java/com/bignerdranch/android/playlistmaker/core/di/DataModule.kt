package com.bignerdranch.android.playlistmaker.core.di
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import androidx.room.Room
import com.bignerdranch.android.playlistmaker.core.application.App
import com.bignerdranch.android.playlistmaker.medialibrary.data.db.AppDatabase
import com.bignerdranch.android.playlistmaker.medialibrary.data.db.dao.FavoriteTracksDao
import com.bignerdranch.android.playlistmaker.search.data.storage.SharedPrefsTracksStorage
import com.bignerdranch.android.playlistmaker.search.domain.api.TrackStorageRepository
import com.bignerdranch.android.playlistmaker.settings.data.storage.sharedprefs.ISettingsStorage
import com.bignerdranch.android.playlistmaker.settings.data.storage.sharedprefs.SharedPrefsSettingsStorage
import com.bignerdranch.android.playlistmaker.sharing.data.ExternalNavigator
import com.bignerdranch.android.playlistmaker.sharing.domain.api.IExternalNavigator
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val dataModule = module {

    single {
        get<Context>().getSharedPreferences(App.PREFERENCES, AppCompatActivity.MODE_PRIVATE)
    }

    single {
        Room.databaseBuilder(androidContext(), AppDatabase::class.java, "database.db")
            .build()
    }

    single<FavoriteTracksDao> { get<AppDatabase>().dao() }

    single<TrackStorageRepository> {
        SharedPrefsTracksStorage (sharedPreferences = get(), favoriteTracksDao = get())
    }

    single<ISettingsStorage> {
        SharedPrefsSettingsStorage(sharedPreferences = get())
    }

    single<IExternalNavigator> {
        ExternalNavigator(get())
    }

    single<SharedPrefsTracksStorage> {
        SharedPrefsTracksStorage(
            sharedPreferences = get(),
            favoriteTracksDao = get())
    }
}