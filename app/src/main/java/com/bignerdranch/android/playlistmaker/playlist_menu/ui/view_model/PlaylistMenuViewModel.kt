package com.bignerdranch.android.playlistmaker.playlist_menu.ui.view_model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bignerdranch.android.playlistmaker.medialibrary.domain.db.PlaylistsInteractor
import com.bignerdranch.android.playlistmaker.medialibrary.domain.models.PlayListTrackModel
import com.bignerdranch.android.playlistmaker.medialibrary.domain.models.PlaylistTrackModelConverter
import com.bignerdranch.android.playlistmaker.playlist_creator.domain.models.PlaylistModel
import com.bignerdranch.android.playlistmaker.playlist_menu.ui.models.PlaylistMenuScreenState
import com.bignerdranch.android.playlistmaker.playlist_menu.ui.models.PlaylistMenuState
import com.bignerdranch.android.playlistmaker.playlist_menu.domain.api.PlaylistDurationCalculator
import com.bignerdranch.android.playlistmaker.search.domain.models.TrackModel
import com.bignerdranch.android.playlistmaker.sharing.domain.api.ISharingInteractor
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch

class PlaylistMenuViewModel(
    private val calculator: PlaylistDurationCalculator,
    private val playlistsInteractor: PlaylistsInteractor,
    private val sharingInteractor: ISharingInteractor
) : ViewModel() {

    private val _contentFlow: MutableSharedFlow<PlaylistMenuState> = MutableSharedFlow(replay = 1)
    val contentFlow = _contentFlow.asSharedFlow()

    private var playlistModel: PlaylistModel? = null
    private var trackFromPlaylist: List<TrackModel> = emptyList()

    fun fillData(playlistId: Int) {
        viewModelScope.launch {
            playlistsInteractor.getPlaylist(playlistId).collect { playlist ->
                playlistModel = playlist
                viewModelScope.launch {
                    val entityList = playlistsInteractor.getTracksFromPlaylist(playlist)
                    trackFromPlaylist = mapedForMesages(playlistModel!!, entityList)
                    refreshState(trackFromPlaylist)
                }

            }
        }
    }

    fun getPlaylist(): PlaylistModel = playlistModel ?: PlaylistModel.emptyPlaylist

    fun getPlaylistDuration(): Int =
        playlistModel?.let { calculator.getTracksDuration(trackFromPlaylist) } ?: EMPTY_VALUE

    fun getTracksCount(): Int = playlistModel?.tracksCount ?: EMPTY_VALUE

    fun deleteTrack(track: TrackModel) {
        viewModelScope.launch {
            playlistModel?.let {
                playlistsInteractor.deleteTrack(
                    it,
                    PlaylistTrackModelConverter().map(track)
                )
            }
        }
    }

    private suspend fun refreshState(trackFromPlaylist: List<TrackModel>) {
        if (trackFromPlaylist.isEmpty()) {
            _contentFlow.emit(
                PlaylistMenuState.Content(
                    getPlaylist(),
                    PlaylistMenuScreenState.Empty
                )
            )
        } else {
            _contentFlow.emit(
                PlaylistMenuState.Content(
                    getPlaylist(),
                    PlaylistMenuScreenState.Content(trackFromPlaylist)
                )
            )
        }
    }

    suspend fun isTracksEmpty(playlist: PlaylistModel): Boolean {
        val countTracks = playlistsInteractor.getTracksFromPlaylist(playlist).size
        return countTracks <= 0

    }

    fun shareClicked() {
        viewModelScope.launch {
            if (isTracksEmpty(playlistModel!!)) {
                _contentFlow.emit(PlaylistMenuState.EmptyShare)
            } else {
                playlistModel?.let { _contentFlow.emit(PlaylistMenuState.Share(it)) }
            }
        }
    }

    fun sharePlaylist(message: String) {
        sharingInteractor.share(message)
    }

    fun deletePlaylist(playlist: PlaylistModel) {
        viewModelScope.launch {
            playlistsInteractor.deletePlaylist(playlist)
        }
    }

    suspend fun getTracksFromPlaylist(playlist: PlaylistModel): List<PlayListTrackModel> {
        return playlistsInteractor.getTracksFromPlaylist(playlist)
    }

    suspend fun mapedForMesages(playlist: PlaylistModel, trackModel: List<PlayListTrackModel>): List<TrackModel> {
        val entityList = playlistsInteractor.getTracksFromPlaylist(playlist)
        val trackModelList = entityList.map { trackModelList ->
            PlaylistTrackModelConverter().map(trackModelList)
        }
        return trackModelList
    }

    companion object {
        private const val EMPTY_VALUE = 0
    }
}