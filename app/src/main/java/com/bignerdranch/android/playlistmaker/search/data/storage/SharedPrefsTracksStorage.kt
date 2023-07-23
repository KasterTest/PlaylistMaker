package com.bignerdranch.android.playlistmaker.search.data.storage

import android.content.SharedPreferences
import com.bignerdranch.android.playlistmaker.medialibrary.data.db.dao.FavoriteTracksDao
import com.bignerdranch.android.playlistmaker.search.domain.api.TrackStorageRepository
import com.bignerdranch.android.playlistmaker.search.domain.models.TrackModel
import com.google.gson.Gson

class SharedPrefsTracksStorage(
    private val sharedPreferences: SharedPreferences,
    private val favoriteTracksDao: FavoriteTracksDao) : TrackStorageRepository {

    companion object {
        private const val SEARCH_HISTORY_KEY = "history_preferences"
    }

    override fun saveHistory(historyList: List<TrackModel>) {
        val json = Gson().toJson(historyList)

        sharedPreferences
            .edit()
            .putString(SEARCH_HISTORY_KEY, json)
            .apply()
    }

    override suspend fun readHistory(): List<TrackModel> {
        val searchHistoryJson = sharedPreferences.getString(SEARCH_HISTORY_KEY, null)
        return if (searchHistoryJson != null) {
            val favoriteTrackIds = favoriteTracksDao.getAllFavoriteTrackIds()
            val tracks = Gson().fromJson(searchHistoryJson, Array<TrackModel>::class.java).toMutableList().map { track ->
                val isFavorite = track.trackId in favoriteTrackIds
                track.copy(isFavorite = isFavorite)
            }
            tracks
        } else {
            mutableListOf()
        }
    }

    override fun clearHistory() {
        sharedPreferences.edit().remove(SEARCH_HISTORY_KEY).apply()
    }

}
