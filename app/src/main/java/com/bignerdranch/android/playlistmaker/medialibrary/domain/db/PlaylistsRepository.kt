package com.bignerdranch.android.playlistmaker.medialibrary.domain.db

import com.bignerdranch.android.playlistmaker.medialibrary.domain.models.PlayListTrackModel
import com.bignerdranch.android.playlistmaker.playlist_creator.domain.models.PlaylistModel
import kotlinx.coroutines.flow.Flow

interface PlaylistsRepository {

    suspend fun createPlaylist(playlist: PlaylistModel)

    suspend fun deletePlaylist(playlist: PlaylistModel)

    suspend fun addTrackToPlaylist(playlist: Int, trackPlaylist: PlayListTrackModel)

    suspend fun getSavedPlaylists(): Flow<List<PlaylistModel>>

    suspend fun getSavedPlaylist(playlistId: Int): Flow<PlaylistModel>

    suspend fun getSavedTrackInPlaylist(): List<PlayListTrackModel>

    suspend fun isPlaylistEmpty (playlist: PlaylistModel, trackPlaylist: PlayListTrackModel): Boolean

    suspend fun deleteTrackFromPlaylist(playlist: PlaylistModel, trackPlaylist: PlayListTrackModel)

    suspend fun getTracksInPlaylist (playlistId: Int) : List<PlayListTrackModel>

    suspend fun getTrackIdByTrack(trackId: String): Int

    suspend fun updatePlaylist(playlist: PlaylistModel)




}