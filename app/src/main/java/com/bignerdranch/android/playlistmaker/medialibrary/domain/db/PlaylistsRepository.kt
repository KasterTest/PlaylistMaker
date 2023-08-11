package com.bignerdranch.android.playlistmaker.medialibrary.domain.db

import com.bignerdranch.android.playlistmaker.playlist_creator.domain.models.PlaylistModel
import kotlinx.coroutines.flow.Flow

interface PlaylistsRepository {

    suspend fun createPlaylist(playlist: PlaylistModel)

    suspend fun deletePlaylist(playlist: PlaylistModel)

    suspend fun updateTracks(playlist: PlaylistModel)

    fun getSavedPlaylists(): Flow<List<PlaylistModel>>

}