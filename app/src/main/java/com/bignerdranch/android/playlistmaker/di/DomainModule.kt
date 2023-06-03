package com.bignerdranch.android.playlistmaker.di

import com.bignerdranch.android.playlistmaker.player.domain.impl.PlayerInteractor
import com.bignerdranch.android.playlistmaker.search.data.network.TrackSearchRepository
import com.bignerdranch.android.playlistmaker.search.domain.api.TrackSearchInteractor
import com.bignerdranch.android.playlistmaker.search.domain.impl.SearchInteractor
import com.bignerdranch.android.playlistmaker.settings.domain.api.ISettingsInteractor
import com.bignerdranch.android.playlistmaker.settings.domain.impl.SettingsInteractor
import com.bignerdranch.android.playlistmaker.sharing.domain.api.ISharingInteractor
import com.bignerdranch.android.playlistmaker.sharing.domain.impl.SharingInteractor
import org.koin.dsl.module

val domainModule = module {

    single<ISettingsInteractor> {
        SettingsInteractor(repository = get())
    }

    single<ISharingInteractor> {
        SharingInteractor(get())
    }

    factory<SearchInteractor> { SearchInteractor(
        searchRepository = get(),
        trackSearchInteractor = get()
    )
    }

    factory<PlayerInteractor>{
        PlayerInteractor(playerRepository = get())
    }

    single<TrackSearchInteractor> {
        TrackSearchRepository()
    }


}