package com.bignerdranch.android.playlistmaker.search.data.network

import com.bignerdranch.android.playlistmaker.search.data.storage.TrackSearchState
import com.bignerdranch.android.playlistmaker.search.domain.api.TrackSearchInteractor
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class TrackSearchRepository() : TrackSearchInteractor {

    private val itunesBaseUrl = "https://itunes.apple.com"

    private val retrofit = Retrofit.Builder()
        .baseUrl(itunesBaseUrl)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val itunesService = retrofit.create(ItunesApiService::class.java)

    override suspend fun searchTracks(term: String): Flow<TrackSearchState> = flow {
        try {
            val response = itunesService.searchTracks(term)
            if (response.results.isEmpty()) {
                emit(TrackSearchState.NotFound)
            } else {
                emit(TrackSearchState.Success(response.results))
            }
        } catch (e: Exception) {
            emit(TrackSearchState.Error(e.message ?: "Ошибка"))
        }
    }
}