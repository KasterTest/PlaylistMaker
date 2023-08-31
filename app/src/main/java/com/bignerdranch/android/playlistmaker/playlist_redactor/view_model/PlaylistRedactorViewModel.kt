package com.bignerdranch.android.playlistmaker.playlist_redactor.view_model

import android.content.Context
import com.bignerdranch.android.playlistmaker.medialibrary.domain.db.PlaylistsInteractor
import com.bignerdranch.android.playlistmaker.playlist_creator.domain.api.NewPlaylistUseCase
import com.bignerdranch.android.playlistmaker.playlist_creator.domain.models.PlaylistModel
import com.bignerdranch.android.playlistmaker.playlist_creator.ui.view_model.NewPlaylistViewModel
import java.net.URI

class PlaylistRedactorViewModel(useCase: NewPlaylistUseCase,
                                private val interactor: PlaylistsInteractor,
                                context: Context
                                ) : NewPlaylistViewModel(useCase, context) {

    private var playlist: PlaylistModel? = null

    override fun onPlaylistNameChanged(playlistName: String?) {
        if (playlistName != null) {
            playlist = playlist?.copy(playlistName = playlistName)
        }
        super.onPlaylistNameChanged(playlistName)
    }

    override fun onPlaylistDescriptionChanged(playlistDescription: String?) {

        if (playlistDescription != null) {
            playlist = playlist?.copy(playlistDescription = playlistDescription)
        }
    }

    override fun saveImageUri(uri: URI) {
        playlist = playlist?.copy(coverImageUrl = uri.toString())
    }

    override fun createPlaylist(): PlaylistModel {
        return this.playlist ?: PlaylistModel.emptyPlaylist
    }

    fun initPlaylist(playlist: PlaylistModel) {
        this.playlist = playlist
    }

    suspend fun updatePlaylist(playlist: PlaylistModel) {
        interactor.updatePlaylist(playlist)
    }
}