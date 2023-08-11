package com.bignerdranch.android.playlistmaker.medialibrary.data.impl

import com.bignerdranch.android.playlistmaker.medialibrary.data.converters.PlaylistDbConverter
import com.bignerdranch.android.playlistmaker.medialibrary.data.db.AppDatabase
import com.bignerdranch.android.playlistmaker.medialibrary.data.db.entity.PlaylistEntity
import com.bignerdranch.android.playlistmaker.medialibrary.domain.db.PlaylistsRepository
import com.bignerdranch.android.playlistmaker.playlist_creator.domain.models.PlaylistModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class PlaylistsRepositoryImpl(
    private val database: AppDatabase,
    private val converter: PlaylistDbConverter,
) : PlaylistsRepository {

    override suspend fun createPlaylist(playlist: PlaylistModel) {
        database.playlistsDao().insertPlaylist(converter.map(playlist))
    }

    override suspend fun deletePlaylist(playlist: PlaylistModel) {
        database.playlistsDao().deletePlaylist(converter.map(playlist))
    }

    override suspend fun updateTracks(playlist: PlaylistModel) {
        database.playlistsDao().updatePlaylist(converter.map(playlist))
    }

    override fun getSavedPlaylists(): Flow<List<PlaylistModel>> {
        return database.playlistsDao().getSavedPlaylists().map { convertFromTrackEntity(it) }
    }

    private fun convertFromTrackEntity(playlists: List<PlaylistEntity>): List<PlaylistModel> {
        return playlists.map { converter.map(it) }
    }
}