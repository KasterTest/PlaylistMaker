package com.bignerdranch.android.playlistmaker

data class Track(val trackId: Int,
                 val trackName: String,
                 val artistName: String,
                 val trackTimeMillis: Int,
                 val collectionName: String,
                 val releaseDate: String,
                 val primaryGenreName: String,
                 val country: String,
                 val artworkUrl100: String)
