package com.bignerdranch.android.playlistmaker.search.domain.api

import com.bignerdranch.android.playlistmaker.search.domain.models.TrackModel


interface SearchRepository {

    suspend fun getSearchHistoryList() : List<TrackModel>

    fun clearSearchHistory()

    suspend fun addTrackToSearchHistory(track: TrackModel)

}
