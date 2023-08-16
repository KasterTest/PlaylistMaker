package com.bignerdranch.android.playlistmaker.medialibrary.ui.models

import com.bignerdranch.android.playlistmaker.playlist_creator.domain.models.PlaylistModel

sealed class PlaylistsScreenState {
    
    object Empty : PlaylistsScreenState()
    
    class Content(val playlists: List<PlaylistModel>) : PlaylistsScreenState()
}