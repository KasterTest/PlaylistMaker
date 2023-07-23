package com.bignerdranch.android.playlistmaker.medialibrary.data.impl

import com.bignerdranch.android.playlistmaker.medialibrary.data.converters.TracksDbConvertor
import com.bignerdranch.android.playlistmaker.medialibrary.data.db.AppDatabase
import com.bignerdranch.android.playlistmaker.medialibrary.data.db.entity.TrackEntity
import com.bignerdranch.android.playlistmaker.medialibrary.domain.db.FavoriteTracksRepository
import com.bignerdranch.android.playlistmaker.search.domain.models.TrackModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class FavoriteTracksRepositoryImpl(
    private val appDatabase: AppDatabase,
    private val tracksDbConvertor: TracksDbConvertor) : FavoriteTracksRepository {
    override suspend fun saveToFavoriteTrack(track: TrackModel) {
        appDatabase
            .dao()
            .insertTrack(tracksDbConvertor.map(track))
    }

    override suspend fun deleteFromFavoriteTrack(track: TrackModel) {
        appDatabase
            .dao()
            .deleteTrack(tracksDbConvertor.map(track).trackId)
    }

    override suspend fun getFavoriteTracks(): Flow<List<TrackModel>> = flow {
        val tracks = appDatabase.dao().getAllFavoriteTracks()
        emit(convertFromTrackEntity(tracks).toList())
    }

    private fun convertFromTrackEntity(tracks: List<TrackEntity>): List<TrackModel> {
        return tracks.map { track -> tracksDbConvertor.map(track) }
    }


}