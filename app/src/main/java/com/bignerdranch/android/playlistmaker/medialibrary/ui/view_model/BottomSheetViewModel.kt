package com.bignerdranch.android.playlistmaker.medialibrary.ui.view_model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bignerdranch.android.playlistmaker.medialibrary.domain.db.PlaylistsInteractor
import com.bignerdranch.android.playlistmaker.medialibrary.domain.models.PlayListTrackModel
import com.bignerdranch.android.playlistmaker.medialibrary.ui.models.BottomSheetState
import com.bignerdranch.android.playlistmaker.playlist_creator.domain.models.PlaylistModel
import com.bignerdranch.android.playlistmaker.search.domain.models.TrackModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class BottomSheetViewModel(
    private val interactor: PlaylistsInteractor,
) : ViewModel() {

    private val _contentFlow = MutableStateFlow<BottomSheetState>(BottomSheetState.Empty)
    val contentFlow: StateFlow<BottomSheetState> = _contentFlow
    var allPlaylists: List<PlaylistModel>? = null

    init {
        fillData()
    }

    fun onPlaylistClicked(playlist: PlaylistModel, track: TrackModel) {
        viewModelScope.launch(Dispatchers.IO) {
            val clickedPlaylistId = getClickedPlaylistId(playlist)
            val playListTracks = getPlaylistTrack()
            val playlistIdForCheck = clickedPlaylistId
            val playlistId = getTableTrackLine(playListTracks, track)

            if (interactor.isTrackAlreadyExists(playListTracks, playlistIdForCheck) && playlistId.contains(playlistIdForCheck)) {
                withContext(Dispatchers.Main) {
                    _contentFlow.value = BottomSheetState.AddedAlready(playlist)
                }
            } else {
                val trackModel = PlayListTrackModel(id = 0, playlistId = playlistId.toMutableList(), track = track)
                interactor.updateCountInPlaylist(playlist)
                if (playListTracks.any { it.track == track }) {
                    interactor.updateTrakInPlaylist(trackModel)
                } else {
                    interactor.addTrackToPlaylist(trackModel)
                }

                withContext(Dispatchers.Main) {
                    _contentFlow.value = BottomSheetState.AddedNow(playlist)
                }
            }
        }
    }

    suspend fun getPlaylistTrack(): List<PlayListTrackModel> {
        return interactor.getPlaylistTracks()
    }

    fun getTableTrackLine(tracklist: List<PlayListTrackModel>, track: TrackModel): List<Int?> {
        return tracklist.firstOrNull { it.track == track }?.playlistId ?: emptyList()
    }

    fun getClickedPlaylistId(playlist: PlaylistModel): Int? {
        return allPlaylists?.indexOfFirst { it == playlist }?.takeIf { it != -1 }?.let { allPlaylists?.get(it)?.id }
    }

    private fun fillData() {
        viewModelScope.launch(Dispatchers.IO) {
            interactor
                .getPlaylists()
                .collect { playlists ->
                    processResult(playlists)
                    allPlaylists = playlists

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