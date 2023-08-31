package com.bignerdranch.android.playlistmaker.medialibrary.domain.db

import com.bignerdranch.android.playlistmaker.medialibrary.domain.models.PlayListTrackModel
import com.bignerdranch.android.playlistmaker.playlist_creator.domain.models.PlaylistModel
import kotlinx.coroutines.flow.Flow

interface PlaylistsInteractor {

    suspend fun getPlaylists(): Flow<List<PlaylistModel>>
    suspend fun getPlaylist(playlistId: Int): Flow<PlaylistModel>
    suspend fun addTrackToPlaylist(playlist: Int, trackPlaylist: PlayListTrackModel)
    suspend fun getPlaylistTracks(): List<PlayListTrackModel>
    suspend fun deleteTrack(playlist: PlaylistModel, trackModel: PlayListTrackModel)
    suspend fun deletePlaylist(playlist: PlaylistModel)
    suspend fun getTracksFromPlaylist (playlist: PlaylistModel) : List<PlayListTrackModel>
    suspend fun isPlaylistEmpty(playlist: PlaylistModel, trackPlaylist: PlayListTrackModel): Boolean
    suspend fun updatePlaylist(playlist: PlaylistModel)

}
