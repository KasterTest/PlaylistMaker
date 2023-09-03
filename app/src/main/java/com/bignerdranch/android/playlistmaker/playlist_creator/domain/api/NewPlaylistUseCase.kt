package com.bignerdranch.android.playlistmaker.playlist_creator.domain.api

import com.bignerdranch.android.playlistmaker.playlist_creator.domain.models.PlaylistModel

interface NewPlaylistUseCase {
    suspend fun create(playlist: PlaylistModel)

    fun saveImageToShared(uri: String)
}