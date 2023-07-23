package com.bignerdranch.android.playlistmaker.medialibrary.domain.db

import com.bignerdranch.android.playlistmaker.search.domain.models.TrackModel
import kotlinx.coroutines.flow.Flow

interface FavoriteTracksInteractor {
    suspend fun addToFavoriteTracks(track: TrackModel)
    suspend fun removeFromFavoriteTracks(track: TrackModel)
    suspend fun getFavoriteTracks(): Flow<List<TrackModel>>
}