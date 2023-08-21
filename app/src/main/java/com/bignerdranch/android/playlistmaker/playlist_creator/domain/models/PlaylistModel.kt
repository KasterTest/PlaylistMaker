package com.bignerdranch.android.playlistmaker.playlist_creator.domain.models


data class PlaylistModel(
    val id: Int,
    val coverImageUrl: String,
    val playlistName: String,
    val playlistDescription:String,
    var tracksCount: Int,
) {
    companion object {
        val emptyPlaylist = PlaylistModel(
            id = 0,
            coverImageUrl = "",
            playlistName = "",
            playlistDescription = "",
            tracksCount = 0,
        )
    }

}