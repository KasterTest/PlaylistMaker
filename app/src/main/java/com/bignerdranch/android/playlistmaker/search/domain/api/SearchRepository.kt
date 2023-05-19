package com.bignerdranch.android.playlistmaker.search.domain.api

import com.bignerdranch.android.playlistmaker.search.domain.models.TrackModel


interface SearchRepository {

    fun getSearchHistoryList() : List<TrackModel>

    fun clearSearchHistory()

    fun addTrackToSearchHistory(track: TrackModel)

}
