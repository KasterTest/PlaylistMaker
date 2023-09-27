package com.bignerdranch.android.playlistmaker.playlist_redactor.view_model

import androidx.lifecycle.viewModelScope
import com.bignerdranch.android.playlistmaker.medialibrary.domain.db.PlaylistsInteractor
import com.bignerdranch.android.playlistmaker.playlist_creator.domain.api.NewPlaylistUseCase
import com.bignerdranch.android.playlistmaker.playlist_creator.domain.models.PlaylistModel
import com.bignerdranch.android.playlistmaker.playlist_creator.ui.view_model.NewPlaylistViewModel
import kotlinx.coroutines.launch
import java.net.URI

class PlaylistRedactorViewModel(useCase: NewPlaylistUseCase,
                                private val interactor: PlaylistsInteractor
                                ) : NewPlaylistViewModel(useCase) {

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

    fun updatePlaylist(playlist: PlaylistModel) {
        viewModelScope.launch {
            interactor.updatePlaylist(playlist)
        }
    }

        fun getSavedImageUri(): String? {
            return interactor.getSavedImageUri()
        }

    }