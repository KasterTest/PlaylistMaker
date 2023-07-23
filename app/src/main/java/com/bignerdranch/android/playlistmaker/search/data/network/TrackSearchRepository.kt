package com.bignerdranch.android.playlistmaker.search.data.network

import com.bignerdranch.android.playlistmaker.medialibrary.data.db.dao.FavoriteTracksDao
import com.bignerdranch.android.playlistmaker.search.data.storage.TrackSearchState
import com.bignerdranch.android.playlistmaker.search.domain.api.TrackSearchInteractor
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class TrackSearchRepository(private val favoriteTracksDao: FavoriteTracksDao) : TrackSearchInteractor {

    private val itunesBaseUrl = "https://itunes.apple.com"

    private val retrofit = Retrofit.Builder()
        .baseUrl(itunesBaseUrl)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val itunesService = retrofit.create(ItunesApiService::class.java)

    override suspend fun searchTracks(term: String): Flow<TrackSearchState> = flow {
        try {
            emit(TrackSearchState.Loading)

            val favoriteTrackIds = withContext(Dispatchers.IO) {
                favoriteTracksDao.getAllFavoriteTrackIds()
            }
            println(favoriteTrackIds)

            val response = itunesService.searchTracks(term)

            if (response.results.isEmpty()) {
                emit(TrackSearchState.NotFound)
            } else {
                val transformedResults = response.results.map { result ->
                    val isFavorite = favoriteTrackIds.contains(result.trackId)
                    result.copy(
                    trackId = result.trackId  ?: "",
                    trackName = result.trackName  ?: "",
                    artistName = result.artistName  ?: "",
                    trackTimeMillis = result.trackTimeMillis ?: 0,
                    collectionName = result.collectionName  ?: "",
                    releaseDate = result.releaseDate  ?: "",
                    primaryGenreName = result.primaryGenreName  ?: "",
                    country = result.country  ?: "",
                    artworkUrl100 = result.artworkUrl100  ?: "",
                    previewUrl = result.previewUrl  ?: "",
                    isFavorite = isFavorite)
                }
                emit(TrackSearchState.Success(transformedResults))
            }
        } catch (e: Exception) {
            e.printStackTrace()
            emit(TrackSearchState.Error(e.message ?: "Ошибка"))
        }
    }.flowOn(Dispatchers.IO)
}
