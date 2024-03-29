package com.bignerdranch.android.playlistmaker.medialibrary.ui.view_model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bignerdranch.android.playlistmaker.medialibrary.domain.db.PlaylistsInteractor
import com.bignerdranch.android.playlistmaker.medialibrary.ui.models.PlaylistsScreenState
import com.bignerdranch.android.playlistmaker.playlist_creator.domain.models.PlaylistModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class PlaylistsViewModel(
    private val interactor: PlaylistsInteractor,
) : ViewModel() {

    private val _contentFlow: MutableStateFlow<PlaylistsScreenState> = MutableStateFlow(PlaylistsScreenState.Empty)
    val contentFlow: StateFlow<PlaylistsScreenState> = _contentFlow

    init {
        fillData()
    }

    private fun fillData() {
        viewModelScope.launch(Dispatchers.IO) {
            interactor
                .getPlaylists()
                .collect { playlists ->
                    processResult(playlists)
                }
        }

    }

    private fun processResult(playlists: List<PlaylistModel>) {
        if (playlists.isEmpty()) {
            _contentFlow.value = (PlaylistsScreenState.Empty)
        } else {
            _contentFlow. value = (PlaylistsScreenState.Content(playlists))
        }
    }
}