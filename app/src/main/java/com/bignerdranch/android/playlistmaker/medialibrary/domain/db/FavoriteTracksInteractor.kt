package com.bignerdranch.android.playlistmaker.medialibrary.domain.db

import com.bignerdranch.android.playlistmaker.search.domain.models.TrackModel
import kotlinx.coroutines.flow.Flow

interface FavoriteTracksInteractor {
    suspend fun toFavoriteTrack(track: TrackModel)
    suspend fun unFavoriteTrack(track: TrackModel)
    suspend fun getFavoriteTracks(): Flow<List<TrackModel>>
}