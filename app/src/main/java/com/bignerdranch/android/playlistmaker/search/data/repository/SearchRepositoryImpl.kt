package com.bignerdranch.android.playlistmaker.search.data.repository



import com.bignerdranch.android.playlistmaker.search.domain.api.SearchRepository
import com.bignerdranch.android.playlistmaker.search.domain.api.TrackStorageRepository
import com.bignerdranch.android.playlistmaker.search.domain.models.TrackModel


class SearchRepositoryImpl (private val tracksStorage: TrackStorageRepository) : SearchRepository {

    override suspend fun getSearchHistoryList(): List<TrackModel> {
        return tracksStorage.readHistory()
    }

    override fun clearSearchHistory() {
        tracksStorage.clearHistory()
    }

    override suspend fun addTrackToSearchHistory(track: TrackModel) {
        var historyList = getSearchHistoryList()
        historyList = historyList.toMutableList()
        if (historyList.contains(track)) {
            historyList.removeAt(historyList.indexOf(track))
            historyList.add(0, track)
        }
        else historyList.add(0, track)
        if (historyList.size > 10) {
            historyList.removeLast()
        }
        historyList = historyList.toList()
        saveSearchHistoryList(historyList)
    }

    private fun saveSearchHistoryList(historyList: List<TrackModel>) {
        tracksStorage.saveHistory(historyList)
    }
}

