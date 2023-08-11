package com.bignerdranch.android.playlistmaker.medialibrary.ui.view_model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bignerdranch.android.playlistmaker.medialibrary.domain.db.PlaylistsInteractor
import com.bignerdranch.android.playlistmaker.medialibrary.ui.models.BottomSheetState
import com.bignerdranch.android.playlistmaker.playlist_creator.domain.models.PlaylistModel
import com.bignerdranch.android.playlistmaker.search.domain.models.TrackModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class BottomSheetViewModel(
    private val interactor: PlaylistsInteractor,
) : ViewModel() {

    private val _contentFlow = MutableStateFlow<BottomSheetState>(BottomSheetState.Empty)
    val contentFlow: StateFlow<BottomSheetState> = _contentFlow

    init {
        fillData()
    }

    fun onPlaylistClicked(playlist: PlaylistModel, track: TrackModel) {
        if (interactor.isTrackAlreadyExists(playlist, track)) {
            _contentFlow.value = BottomSheetState.AddedAlready(playlist)
        } else {
            viewModelScope.launch(Dispatchers.IO) {
                interactor.addTrackToPlaylist(playlist, track)
                _contentFlow.value = BottomSheetState.AddedNow(playlist)
            }
        }
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
            _contentFlow.value = BottomSheetState.Empty
        } else {
            _contentFlow.value = BottomSheetState.Content(playlists)
        }
    }
}