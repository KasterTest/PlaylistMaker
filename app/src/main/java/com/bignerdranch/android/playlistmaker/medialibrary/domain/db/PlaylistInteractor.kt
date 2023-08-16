package com.bignerdranch.android.playlistmaker.medialibrary.domain.db

import com.bignerdranch.android.playlistmaker.medialibrary.domain.models.PlayListTrackModel
import com.bignerdranch.android.playlistmaker.playlist_creator.domain.models.PlaylistModel
import kotlinx.coroutines.flow.Flow

interface PlaylistsInteractor {

    suspend fun getPlaylists(): Flow<List<PlaylistModel>>
    fun isTrackAlreadyExists(playListTrackModels: List<PlayListTrackModel>, playlistIdToCheck: Int?): Boolean
    suspend fun addTrackToPlaylist(trackModel: PlayListTrackModel)
    suspend fun getPlaylistTracks(): List<PlayListTrackModel>
    suspend fun updateTrakInPlaylist(trackModel: PlayListTrackModel)
    suspend fun updateCountInPlaylist(playlist: PlaylistModel)

}
