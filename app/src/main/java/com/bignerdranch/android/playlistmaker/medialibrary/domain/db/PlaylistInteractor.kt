package com.bignerdranch.android.playlistmaker.medialibrary.domain.db

import com.bignerdranch.android.playlistmaker.playlist_creator.domain.models.PlaylistModel
import com.bignerdranch.android.playlistmaker.search.domain.models.TrackModel
import kotlinx.coroutines.flow.Flow

interface PlaylistsInteractor {

    fun getPlaylists(): Flow<List<PlaylistModel>>
    fun isTrackAlreadyExists(playlist: PlaylistModel, track: TrackModel): Boolean
    suspend fun addTrackToPlaylist(playlist: PlaylistModel, track: TrackModel)
}
