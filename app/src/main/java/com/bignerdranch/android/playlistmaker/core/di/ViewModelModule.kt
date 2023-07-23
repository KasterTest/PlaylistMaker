package com.bignerdranch.android.playlistmaker.core.di

import com.bignerdranch.android.playlistmaker.medialibrary.ui.view_model.FavoriteTracksViewModel
import com.bignerdranch.android.playlistmaker.player.ui.view_model.PlayerViewModel
import com.bignerdranch.android.playlistmaker.search.ui.view_model.SearchViewModel
import com.bignerdranch.android.playlistmaker.settings.ui.view_model.SettingsViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {

    viewModel<PlayerViewModel>{
            PlayerViewModel(
                playerInteractor = get(),
                favoriteTrackInteractor = get()
            )
    }
    viewModel<SearchViewModel> {
        SearchViewModel(
            searchInteractor = get()
        )
    }
    viewModel<SettingsViewModel> {
        SettingsViewModel(
            settingsInteractor = get(),
            sharingInteractor = get()
        )
    }
    viewModel<FavoriteTracksViewModel>{
      FavoriteTracksViewModel(
          favoriteTracksInteractor = get()
      )
    }

}