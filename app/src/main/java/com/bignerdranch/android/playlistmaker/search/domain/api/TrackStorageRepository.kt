package com.bignerdranch.android.playlistmaker.search.domain.api

import com.bignerdranch.android.playlistmaker.search.domain.models.TrackModel

interface TrackStorageRepository {
    fun saveHistory(historyList: List<TrackModel>)
    fun readHistory(): List<TrackModel>
    fun clearHistory()
}