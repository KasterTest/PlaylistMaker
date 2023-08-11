package com.bignerdranch.android.playlistmaker.playlist_creator.domain.impl

import com.bignerdranch.android.playlistmaker.medialibrary.domain.db.PlaylistsRepository
import com.bignerdranch.android.playlistmaker.playlist_creator.domain.api.NewPlaylistUseCase
import com.bignerdranch.android.playlistmaker.playlist_creator.domain.models.PlaylistModel


class NewPlaylistUseCaseImpl(
    private val repository: PlaylistsRepository,
) : NewPlaylistUseCase {
    
    override suspend fun create(playlist: PlaylistModel) {
        repository.createPlaylist(playlist)
    }
}