package com.bignerdranch.android.playlistmaker.medialibrary.domain.db

import com.bignerdranch.android.playlistmaker.medialibrary.domain.models.PlayListTrackModel
import com.bignerdranch.android.playlistmaker.playlist_creator.domain.models.PlaylistModel
import kotlinx.coroutines.flow.Flow

interface PlaylistsRepository {

    suspend fun createPlaylist(playlist: PlaylistModel)

    suspend fun deletePlaylist(playlist: PlaylistModel)

    suspend fun addTrackToPlaylist(trackPlaylist: PlayListTrackModel)

    suspend fun updateTrackInPlaylist(playlist: PlayListTrackModel)

    suspend fun getSavedPlaylists(): Flow<List<PlaylistModel>>

    suspend fun getSavedTrackInPlaylist(): List<PlayListTrackModel>

    suspend fun updatePlaylistTrackCount(playlist: PlaylistModel)

}