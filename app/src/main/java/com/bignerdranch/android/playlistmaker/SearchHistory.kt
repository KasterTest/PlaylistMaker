package com.bignerdranch.android.playlistmaker
import android.content.SharedPreferences
import com.google.gson.Gson

class SearchHistory(private val sharedPreferences: SharedPreferences) {

    companion object {
        private const val MAX_HISTORY_SIZE = 10
        const val SEARCH_HISTORY_KEY = "search_history"
    }

    fun addTrack(track: Track) {
        val historyList = getSearchHistoryList()
        historyList.removeIf { it.trackId == track.trackId }
        historyList.add(0, track)
        if (historyList.size > MAX_HISTORY_SIZE) {
            historyList.removeLast()
        }
        saveSearchHistoryList(historyList)
    }

    fun getSearchHistoryList(): MutableList<Track> {
        val searchHistoryJson = sharedPreferences.getString(SEARCH_HISTORY_KEY, null)
        return if (searchHistoryJson != null) {
            Gson().fromJson(searchHistoryJson, Array<Track>::class.java).toMutableList()
        } else {
            mutableListOf()
        }
    }

    fun clearSearchHistory() {
        sharedPreferences.edit().remove(SEARCH_HISTORY_KEY).apply()
    }

    private fun saveSearchHistoryList(historyList: MutableList<Track>) {
        val searchHistoryJson = Gson().toJson(historyList)
        sharedPreferences.edit().putString(SEARCH_HISTORY_KEY, searchHistoryJson).apply()
    }

}