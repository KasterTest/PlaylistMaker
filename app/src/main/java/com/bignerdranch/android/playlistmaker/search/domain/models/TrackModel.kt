package com.bignerdranch.android.playlistmaker.search.domain.models

data class TrackModel(val trackId: String,
                      val trackName: String,
                      val artistName: String,
                      val trackTimeMillis: Int,
                      val collectionName: String,
                      val releaseDate: String,
                      val primaryGenreName: String,
                      val country: String,
                      val artworkUrl100: String,
                      val previewUrl: String,
                      var isFavorite: Boolean = false )
