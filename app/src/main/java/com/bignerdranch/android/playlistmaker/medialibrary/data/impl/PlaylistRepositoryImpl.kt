package com.bignerdranch.android.playlistmaker.medialibrary.data.impl

import com.bignerdranch.android.playlistmaker.medialibrary.data.converters.PlaylistDbConverter
import com.bignerdranch.android.playlistmaker.medialibrary.data.converters.PlaylistTrackDbConverter
import com.bignerdranch.android.playlistmaker.medialibrary.data.db.AppDatabase
import com.bignerdranch.android.playlistmaker.medialibrary.data.db.entity.PlaylistEntity
import com.bignerdranch.android.playlistmaker.medialibrary.domain.db.PlaylistsRepository
import com.bignerdranch.android.playlistmaker.medialibrary.domain.models.PlayListTrackModel
import com.bignerdranch.android.playlistmaker.playlist_creator.domain.models.PlaylistModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class PlaylistsRepositoryImpl(
    private val database: AppDatabase,
    private val playlistConverter: PlaylistDbConverter,
    private val playlistTrackConverter: PlaylistTrackDbConverter
) : PlaylistsRepository {


    override suspend fun createPlaylist(playlist: PlaylistModel) {
        database.playlistsDao().insertPlaylist(playlistConverter.map(playlist))
    }

    override suspend fun deletePlaylist(playlist: PlaylistModel) {
        database.playlistsDao().deletePlaylist(playlistConverter.map(playlist))
    }

    override suspend fun addTrackToPlaylist(playlist: PlayListTrackModel) {
        database.playlistTrackDao().insert(playlistTrackConverter.map(playlist))
    }

    override suspend fun updateTrackInPlaylist(playlist: PlayListTrackModel) {
        val updateTrackConvert = playlistTrackConverter.map(playlist)
        database.playlistTrackDao().updatePlaylistTrack(
            updateTrackConvert.playlistId,
            updateTrackConvert.track)
    }

    override suspend fun getSavedPlaylists(): Flow<List<PlaylistModel>> {
        return database.playlistsDao().getSavedPlaylists().map { convertFromTrackEntity(it) }
    }

    override suspend fun getSavedTrackInPlaylist(): List<PlayListTrackModel> {
        val playlistTrackEntities = database.playlistTrackDao().getTracksFromPlaylist()
        return playlistTrackEntities.map { playlistTrackConverter.map(it) }
    }

    private fun convertFromTrackEntity(playlists: List<PlaylistEntity>): List<PlaylistModel> {
        return playlists.map { playlistConverter.map(it) }
    }

    override suspend fun updatePlaylistTrackCount(playlist: PlaylistModel) {
        val updateCountConvert = playlistConverter.map(playlist)
        database.playlistsDao().updatePlaylistTrackCount(
            updateCountConvert.id,
            updateCountConvert.countTracks)
    }



}