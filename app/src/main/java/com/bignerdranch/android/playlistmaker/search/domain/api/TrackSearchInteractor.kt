package com.bignerdranch.android.playlistmaker.search.domain.api

import com.bignerdranch.android.playlistmaker.search.data.storage.TrackSearchState

interface TrackSearchInteractor {
    fun searchTracks(term: String, callback: (TrackSearchState) -> Unit)
}