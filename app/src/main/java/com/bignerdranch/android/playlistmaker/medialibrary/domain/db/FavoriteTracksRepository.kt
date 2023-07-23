package com.bignerdranch.android.playlistmaker.medialibrary.domain.db

import com.bignerdranch.android.playlistmaker.search.domain.models.TrackModel
import kotlinx.coroutines.flow.Flow

interface FavoriteTracksRepository {

    suspend fun saveToFavoriteTrack(track: TrackModel)
    suspend fun deleteFromFavoriteTrack(track: TrackModel)
    suspend fun getFavoriteTracks(): Flow<List<TrackModel>>

}