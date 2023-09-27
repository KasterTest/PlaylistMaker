package com.bignerdranch.android.playlistmaker.playlist_menu.ui.models

import com.bignerdranch.android.playlistmaker.playlist_creator.domain.models.PlaylistModel

sealed interface ShareState {
    object Empty : ShareState
    class Content(val playlist: PlaylistModel) : ShareState
}