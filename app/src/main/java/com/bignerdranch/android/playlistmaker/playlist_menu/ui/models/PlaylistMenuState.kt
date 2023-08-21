package com.bignerdranch.android.playlistmaker.playlist_menu.ui.models


import com.bignerdranch.android.playlistmaker.playlist_creator.domain.models.PlaylistModel



sealed interface PlaylistMenuState {
    object DefaultState : PlaylistMenuState
    object EmptyShare : PlaylistMenuState
    data class Content(val content: PlaylistModel, val bottomListState: PlaylistMenuScreenState) : PlaylistMenuState
    class Share(val playlist: PlaylistModel) : PlaylistMenuState
}