package com.bignerdranch.android.playlistmaker.playlist_menu.ui.models

sealed interface PlaylistMenuScreenState {

    object Empty : PlaylistMenuScreenState

    class Content<T>(val contentList: List<T>) : PlaylistMenuScreenState
}