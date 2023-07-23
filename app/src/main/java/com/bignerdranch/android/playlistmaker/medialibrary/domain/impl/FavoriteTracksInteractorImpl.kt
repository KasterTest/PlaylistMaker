package com.bignerdranch.android.playlistmaker.medialibrary.domain.impl

import com.bignerdranch.android.playlistmaker.medialibrary.domain.db.FavoriteTracksInteractor
import com.bignerdranch.android.playlistmaker.medialibrary.domain.db.FavoriteTracksRepository
import com.bignerdranch.android.playlistmaker.search.domain.models.TrackModel
import kotlinx.coroutines.flow.Flow

class FavoriteTracksInteractorImpl(
    private val favoriteTracksRepository: FavoriteTracksRepository) : FavoriteTracksInteractor {
    override suspend fun toFavoriteTrack(track: TrackModel) {
        favoriteTracksRepository.saveToFavoriteTrack(track)
    }

    override suspend fun unFavoriteTrack(track: TrackModel) {
        favoriteTracksRepository.deleteFromFavoriteTrack(track)
    }

    override suspend fun getFavoriteTracks(): Flow<List<TrackModel>> {
        return favoriteTracksRepository.getFavoriteTracks()
    }
}