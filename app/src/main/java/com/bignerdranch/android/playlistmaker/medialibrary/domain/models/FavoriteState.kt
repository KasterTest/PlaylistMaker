package com.bignerdranch.android.playlistmaker.medialibrary.domain.models

import com.bignerdranch.android.playlistmaker.search.domain.models.TrackModel

sealed interface FavoriteState {
    object Empty : FavoriteState
    data class FavoriteTracks(val trackList: List<TrackModel>) : FavoriteState
}