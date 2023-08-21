package com.bignerdranch.android.playlistmaker.medialibrary.domain.models

class PlayListTrackModel(
    val id: Int = 0,
    val trackId: String,
    val trackName: String,
    val artistName: String,
    val trackTimeMillis: Int,
    val collectionName: String,
    val releaseDate: String,
    val primaryGenreName: String,
    val country: String,
    val artworkUrl100: String,
    val previewUrl: String
    )