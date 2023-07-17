package com.bignerdranch.android.playlistmaker.search.domain.impl

import com.bignerdranch.android.playlistmaker.search.data.storage.TrackSearchState
import com.bignerdranch.android.playlistmaker.search.domain.api.SearchRepository
import com.bignerdranch.android.playlistmaker.search.domain.api.TrackSearchInteractor
import com.bignerdranch.android.playlistmaker.search.domain.models.TrackModel
import kotlinx.coroutines.flow.Flow

class SearchInteractor (private val searchRepository: SearchRepository, private val trackSearchInteractor: TrackSearchInteractor)
    {

        suspend fun getSearchHistoryList() : List<TrackModel> {
            return searchRepository.getSearchHistoryList()
        }

        fun clearSearchHistory() {
            searchRepository.clearSearchHistory()
        }

        suspend fun addTrackToSearchHistory(track: TrackModel) {
            searchRepository.addTrackToSearchHistory(track)
        }
        suspend fun searchTracks(term: String): Flow<TrackSearchState> =
                trackSearchInteractor.searchTracks(term)
    }
