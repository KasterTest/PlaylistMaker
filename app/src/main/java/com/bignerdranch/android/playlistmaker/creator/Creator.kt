package com.bignerdranch.android.playlistmaker.creator

import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import com.bignerdranch.android.playlistmaker.application.App
import com.bignerdranch.android.playlistmaker.player.data.PlayerRepositoryImpl
import com.bignerdranch.android.playlistmaker.player.domain.api.PlayerRepository
import com.bignerdranch.android.playlistmaker.player.domain.impl.PlayerInteractor
import com.bignerdranch.android.playlistmaker.search.data.repository.SearchRepositoryImpl
import com.bignerdranch.android.playlistmaker.search.data.network.TrackSearchRepository
import com.bignerdranch.android.playlistmaker.search.data.storage.SharedPrefsTracksStorage
import com.bignerdranch.android.playlistmaker.search.domain.api.SearchRepository
import com.bignerdranch.android.playlistmaker.search.domain.api.TrackSearchInteractor
import com.bignerdranch.android.playlistmaker.search.domain.impl.SearchInteractor
import com.bignerdranch.android.playlistmaker.settings.data.repository.SettingsRepository
import com.bignerdranch.android.playlistmaker.settings.data.storage.sharedprefs.ISettingsStorage
import com.bignerdranch.android.playlistmaker.settings.data.storage.sharedprefs.SharedPrefsSettingsStorage
import com.bignerdranch.android.playlistmaker.settings.domain.api.ISettingsInteractor
import com.bignerdranch.android.playlistmaker.settings.domain.api.ISettingsReporitory
import com.bignerdranch.android.playlistmaker.settings.domain.impl.SettingsInteractor
import com.bignerdranch.android.playlistmaker.sharing.data.ExternalNavigator
import com.bignerdranch.android.playlistmaker.sharing.domain.api.IExternalNavigator
import com.bignerdranch.android.playlistmaker.sharing.domain.api.ISharingInteractor
import com.bignerdranch.android.playlistmaker.sharing.domain.impl.SharingInteractor


object Creator {

    fun provideSearchInteractor(context: Context) : SearchInteractor  {
        return SearchInteractor(
            searchRepository = getTrackRepository(context),
            trackSearchInteractor = getNetworkClient())
    }

    private fun getTrackRepository(context: Context): SearchRepository {
        return SearchRepositoryImpl(
            tracksStorage = getTracksStorage(context),
        )
    }

    private fun getNetworkClient(): TrackSearchInteractor {
        return TrackSearchRepository()
    }

    private fun getTracksStorage(context: Context): SharedPrefsTracksStorage {
        return SharedPrefsTracksStorage(getSharedPreferences(context))
    }

    private fun getSharedPreferences(context: Context): SharedPreferences {
        return context.getSharedPreferences(App.PREFERENCES, AppCompatActivity.MODE_PRIVATE)
    }


    fun provideSettingsInteractor(context: Context): ISettingsInteractor {
        return SettingsInteractor(repository = getSettingsRepository(context))

    }

    private fun getSettingsRepository(context: Context): ISettingsReporitory {
        return SettingsRepository(storage = getSettingsStorage(context))
    }

    private fun getSettingsStorage(context: Context): ISettingsStorage {
        return SharedPrefsSettingsStorage(
            sharedPreferences = getSharedPreferences(context)
        )
    }

    fun provideSharingInteractor(context: Context): ISharingInteractor {
        return SharingInteractor(
            externalNavigator = getExternalNavigator(context)
        )
    }

    private fun getExternalNavigator(context: Context): IExternalNavigator {
        return ExternalNavigator(context = context)
    }

    fun provideMediaInteractor(trackUrl: String): PlayerInteractor {
        return PlayerInteractor(getAudioPlayer(trackUrl))
    }

    private fun getAudioPlayer(trackUrl: String): PlayerRepository {
        return PlayerRepositoryImpl(url = trackUrl)
    }

}



