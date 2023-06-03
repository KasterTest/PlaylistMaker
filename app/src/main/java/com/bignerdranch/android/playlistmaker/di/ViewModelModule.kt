package com.bignerdranch.android.playlistmaker.di

import com.bignerdranch.android.playlistmaker.main.ui.view_model.MainViewModel
import com.bignerdranch.android.playlistmaker.player.ui.view_model.PlayerViewModel
import com.bignerdranch.android.playlistmaker.search.ui.view_model.SearchViewModel
import com.bignerdranch.android.playlistmaker.settings.ui.view_model.SettingsViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    viewModel<MainViewModel>{
        MainViewModel()
    }
    viewModel<PlayerViewModel>{
            PlayerViewModel(
                playerInteractor = get()
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

}