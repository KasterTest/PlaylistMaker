package com.bignerdranch.android.playlistmaker.core.di

import com.bignerdranch.android.playlistmaker.medialibrary.domain.db.FavoriteTracksInteractor
import com.bignerdranch.android.playlistmaker.medialibrary.domain.db.PlaylistsInteractor
import com.bignerdranch.android.playlistmaker.medialibrary.domain.impl.FavoriteTracksInteractorImpl
import com.bignerdranch.android.playlistmaker.medialibrary.domain.impl.PlaylistInteractorImpl
import com.bignerdranch.android.playlistmaker.player.domain.impl.PlayerInteractor
import com.bignerdranch.android.playlistmaker.playlist_creator.domain.api.NewPlaylistUseCase
import com.bignerdranch.android.playlistmaker.playlist_creator.domain.impl.NewPlaylistUseCaseImpl
import com.bignerdranch.android.playlistmaker.search.data.network.TrackSearchRepository
import com.bignerdranch.android.playlistmaker.search.domain.api.TrackSearchInteractor
import com.bignerdranch.android.playlistmaker.search.domain.impl.SearchInteractor
import com.bignerdranch.android.playlistmaker.playlist_menu.domain.api.PlaylistDurationCalculator
import com.bignerdranch.android.playlistmaker.playlist_menu.domain.impl.PlaylistDurationCalculatorImpl
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
        TrackSearchRepository(favoriteTracksDao = get())
    }

    single<FavoriteTracksInteractor> {
       FavoriteTracksInteractorImpl(favoriteTracksRepository = get())
    }

    single<PlaylistsInteractor> {
        PlaylistInteractorImpl(
            repository = get(),
            messageCreatorRepository = get()
        )
    }

    single<NewPlaylistUseCase> {
        NewPlaylistUseCaseImpl(
            repository = get()
        )
    }

    single<PlaylistDurationCalculator> {
        PlaylistDurationCalculatorImpl()
    }

}