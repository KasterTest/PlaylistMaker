package com.bignerdranch.android.playlistmaker.search.domain.api

import com.bignerdranch.android.playlistmaker.search.data.storage.TrackSearchState
import kotlinx.coroutines.flow.Flow

interface TrackSearchInteractor {
    suspend fun searchTracks(term: String): Flow<TrackSearchState>
}