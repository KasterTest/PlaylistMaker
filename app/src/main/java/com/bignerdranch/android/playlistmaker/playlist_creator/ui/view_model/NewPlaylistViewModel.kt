package com.bignerdranch.android.playlistmaker.playlist_creator.ui.view_model

import android.Manifest
import android.os.Build
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bignerdranch.android.playlistmaker.playlist_creator.domain.api.NewPlaylistUseCase
import com.bignerdranch.android.playlistmaker.playlist_creator.domain.models.PermissionResultState
import com.bignerdranch.android.playlistmaker.playlist_creator.ui.models.ScreenState
import com.bignerdranch.android.playlistmaker.playlist_creator.domain.models.PlaylistModel
import com.markodevcic.peko.PermissionRequester
import com.markodevcic.peko.PermissionResult
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import java.net.URI

open class NewPlaylistViewModel(private val newPlaylistUseCase: NewPlaylistUseCase) : ViewModel() {

    private val _screenStateFlow: MutableSharedFlow<ScreenState> = MutableSharedFlow(
        replay = 1, onBufferOverflow = BufferOverflow.DROP_OLDEST
    )

    val screenStateFlow = _screenStateFlow.asSharedFlow()

    private val _permissionStateFlow = MutableSharedFlow<PermissionResultState>(
        replay = 1, onBufferOverflow = BufferOverflow.DROP_OLDEST
    )
    val permissionStateFlow = _permissionStateFlow.asSharedFlow()

    private var coverImageUrl = ""
    private var playlistName = ""
    private var playlistDescription = ""
    private var tracksCount = 0

    private val register = PermissionRequester.instance()

    fun onPlaylistCoverClicked() {
        viewModelScope.launch {
            if (Build.VERSION.SDK_INT >= 33) {
                register.request(Manifest.permission.READ_MEDIA_IMAGES)
            } else {
                register.request(Manifest.permission.READ_EXTERNAL_STORAGE)
            }.collect { result ->
                when (result) {
                    is PermissionResult.Granted -> {
                        _permissionStateFlow.emit(PermissionResultState.GRANTED)
                    }

                    is PermissionResult.Denied.NeedsRationale -> _permissionStateFlow.emit(
                        PermissionResultState.NEEDS_RATIONALE
                    )

                    is PermissionResult.Denied.DeniedPermanently -> _permissionStateFlow.emit(
                        PermissionResultState.DENIED_PERMANENTLY
                    )

                    PermissionResult.Cancelled -> return@collect
                }
            }
        }
    }

    open fun onPlaylistNameChanged(playlistName: String?) {

        if (playlistName != null) {
            this.playlistName = playlistName
        }

        if (!playlistName.isNullOrEmpty()) {
            _screenStateFlow.tryEmit(ScreenState.HasContent())

        } else _screenStateFlow.tryEmit(ScreenState.Empty())
    }

    open fun onPlaylistDescriptionChanged(playlistDescription: String?) {

        if (playlistDescription != null) {
            this.playlistDescription = playlistDescription
        }

    }

    fun onCreateBtnClicked() {
        viewModelScope.launch {
            newPlaylistUseCase.create(
                PlaylistModel(
                    id = 0,
                    coverImageUrl = coverImageUrl,
                    playlistName = playlistName,
                    playlistDescription = playlistDescription,
                    tracksCount = tracksCount
                )
            )
            _screenStateFlow.emit(ScreenState.AllowedToGoOut)
        }
    }

    open fun saveImageUri(uri: URI) {
        coverImageUrl = uri.toString()
    }

    fun onBackPressed() {

        if (coverImageUrl.isNotEmpty() || playlistName.isNotEmpty() || playlistDescription.isNotEmpty()) {
            _screenStateFlow.tryEmit(ScreenState.NeedsToAsk)
        } else {
            _screenStateFlow.tryEmit(ScreenState.AllowedToGoOut)
        }
    }

    open fun createPlaylist(): PlaylistModel {
        return PlaylistModel(
            id = 0,
            coverImageUrl = coverImageUrl,
            playlistName = playlistName,
            playlistDescription = playlistDescription,
            tracksCount = tracksCount
        )
    }

    fun saveImageToShared(uri: String){
        newPlaylistUseCase.saveImageToShared(uri)
    }

}

