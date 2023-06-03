package com.bignerdranch.android.playlistmaker.di

import com.bignerdranch.android.playlistmaker.player.data.PlayerRepositoryImpl
import com.bignerdranch.android.playlistmaker.player.domain.api.PlayerRepository
import com.bignerdranch.android.playlistmaker.search.data.repository.SearchRepositoryImpl
import com.bignerdranch.android.playlistmaker.search.domain.api.SearchRepository
import com.bignerdranch.android.playlistmaker.settings.data.repository.SettingsRepository
import com.bignerdranch.android.playlistmaker.settings.domain.api.ISettingsReporitory
import org.koin.dsl.module

val repositoryModule = module {


    factory<SearchRepository> {
        SearchRepositoryImpl(tracksStorage = get())
    }

    factory<PlayerRepository> {
         PlayerRepositoryImpl(url = get())
    }

    single<ISettingsReporitory> {
        SettingsRepository(storage = get())
    }


}

