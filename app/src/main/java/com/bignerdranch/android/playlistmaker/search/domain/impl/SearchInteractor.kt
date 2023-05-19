package com.bignerdranch.android.playlistmaker.search.domain.impl

import com.bignerdranch.android.playlistmaker.search.data.storage.TrackSearchState
import com.bignerdranch.android.playlistmaker.search.domain.api.SearchRepository
import com.bignerdranch.android.playlistmaker.search.domain.api.TrackSearchInteractor
import com.bignerdranch.android.playlistmaker.search.domain.models.TrackModel

class SearchInteractor (private val searchRepository: SearchRepository, private val trackSearchInteractor: TrackSearchInteractor)
    {

    fun getSearchHistoryList() : List<TrackModel> {
        return searchRepository.getSearchHistoryList()
    }

    fun clearSearchHistory() {
        searchRepository.clearSearchHistory()
    }

    fun addTrackToSearchHistory(track: TrackModel) {
        searchRepository.addTrackToSearchHistory(track)
    }

    fun searchTracks(term: String, callback: (TrackSearchState) -> Unit) {
        trackSearchInteractor.searchTracks(term, callback)
    }

}