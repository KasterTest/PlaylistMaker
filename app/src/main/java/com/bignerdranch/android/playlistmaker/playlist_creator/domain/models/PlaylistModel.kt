package com.bignerdranch.android.playlistmaker.playlist_creator.domain.models

import com.bignerdranch.android.playlistmaker.search.domain.models.TrackModel

data class PlaylistModel(
    val id: Int,
    val coverImageUrl: String,
    val playlistName: String,
    val playlistDescription:String,
    var trackList: List<TrackModel>,
    var tracksCount: Int,
)