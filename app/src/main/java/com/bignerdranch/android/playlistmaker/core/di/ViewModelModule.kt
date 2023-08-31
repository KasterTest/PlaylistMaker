package com.bignerdranch.android.playlistmaker.core.di

import com.bignerdranch.android.playlistmaker.medialibrary.ui.view_model.BottomSheetViewModel
import com.bignerdranch.android.playlistmaker.medialibrary.ui.view_model.FavoriteTracksViewModel
import com.bignerdranch.android.playlistmaker.medialibrary.ui.view_model.PlaylistsViewModel
import com.bignerdranch.android.playlistmaker.player.ui.view_model.PlayerViewModel
import com.bignerdranch.android.playlistmaker.playlist_creator.ui.view_model.NewPlaylistViewModel
import com.bignerdranch.android.playlistmaker.playlist_menu.ui.view_model.PlaylistMenuViewModel
import com.bignerdranch.android.playlistmaker.playlist_redactor.view_model.PlaylistRedactorViewModel
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

    viewModel<NewPlaylistViewModel>{
        NewPlaylistViewModel(
            newPlaylistUseCase = get(),
            context = get()
        )
    }

    viewModel<BottomSheetViewModel>{
        BottomSheetViewModel(
            interactor = get()
        )
    }

    viewModel<PlaylistsViewModel>{
        PlaylistsViewModel(
           interactor = get()
        )
    }

    viewModel<PlaylistRedactorViewModel>{
        PlaylistRedactorViewModel(
            useCase = get(),
            interactor = get(),
            context = get()
        )
    }

    viewModel<PlaylistMenuViewModel>{
        PlaylistMenuViewModel(
            calculator = get(),
            playlistsInteractor = get(),
            sharingInteractor = get()
        )
    }



}