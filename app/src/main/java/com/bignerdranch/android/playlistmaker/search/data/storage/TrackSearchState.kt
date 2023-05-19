package com.bignerdranch.android.playlistmaker.search.data.storage

import com.bignerdranch.android.playlistmaker.search.domain.models.TrackModel

sealed class TrackSearchState {
    object Loading : TrackSearchState()
    data class Success(val tracks: List<TrackModel>) : TrackSearchState()
    object NotFound : TrackSearchState()
    data class Error(val message: String) : TrackSearchState()
    class StateVisibleHistory (val tracks : List<TrackModel>) : TrackSearchState()
}