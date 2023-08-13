package com.bignerdranch.android.playlistmaker.playlist_creator.domain.models

data class PlaylistModel(
    val id: Int,
    val coverImageUrl: String,
    val playlistName: String,
    val playlistDescription:String,
    var tracksCount: Int,
)