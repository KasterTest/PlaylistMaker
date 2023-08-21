package com.bignerdranch.android.playlistmaker.medialibrary.data.impl

import com.bignerdranch.android.playlistmaker.medialibrary.data.converters.PlaylistDbConverter
import com.bignerdranch.android.playlistmaker.medialibrary.data.converters.PlaylistTrackDbConverter
import com.bignerdranch.android.playlistmaker.medialibrary.data.db.AppDatabase
import com.bignerdranch.android.playlistmaker.medialibrary.data.db.entity.PlaylistAndTrackRefEntity
import com.bignerdranch.android.playlistmaker.medialibrary.data.db.entity.PlaylistEntity
import com.bignerdranch.android.playlistmaker.medialibrary.domain.db.PlaylistsRepository
import com.bignerdranch.android.playlistmaker.medialibrary.domain.models.PlayListTrackModel
import com.bignerdranch.android.playlistmaker.playlist_creator.domain.models.PlaylistModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class PlaylistsRepositoryImpl(
    private val database: AppDatabase,
    private val playlistConverter: PlaylistDbConverter,
    private val playlistTrackConverter: PlaylistTrackDbConverter,
) : PlaylistsRepository {


    override suspend fun createPlaylist(playlist: PlaylistModel) {
        database.playlistsDao().insertPlaylist(playlistConverter.map(playlist))
    }

    override suspend fun deletePlaylist(playlist: PlaylistModel) {
        val playlistId = playlistConverter.map(playlist).id
        database.playlistAndTrackRefDao().deleteAllTracksForPlaylist(playlistId)
        database.playlistsDao().deletePlaylist(playlistConverter.map(playlist))
    }

    override suspend fun deleteTrackFromPlaylist(playlist: PlaylistModel, trackPlaylist: PlayListTrackModel) {
        val playlistId = playlistConverter.map(playlist).id
        val oneTrack = playlistTrackConverter.map(trackPlaylist)
        val tracksInPlaylist = database.playlistTrackDao().getTracksFromPlaylist()
        val targetTrackId: String = oneTrack.trackId

        val foundTrack = tracksInPlaylist.find { it.trackId == targetTrackId }
        if (foundTrack != null) {
            database.playlistAndTrackRefDao().deleteTrackFromTrackPlaylist(playlistId, foundTrack.id)
            val playlist = database.playlistTrackDao().getTracksInPlaylist(playlistId)
            val newTrackCount = playlist.size
            newTrackCount?.let {
                database.playlistsDao().updateTrackCount(playlistId, newTrackCount)
            }

            val playlistAndTrack = database.playlistAndTrackRefDao().getPlaylistAndTracksRef()
            val isExistTrackInDB = playlistAndTrack.any { it.trackId == foundTrack.id }
            if (!isExistTrackInDB) {
                database.playlistTrackDao().deleteTrackByTrackId(foundTrack.trackId)
            }
        }
    }

    override suspend fun getTracksInPlaylist(playlistId: Int): List<PlayListTrackModel> {
        val entityList = database.playlistTrackDao().getTracksInPlaylist(playlistId)
        val trackModelList = entityList.map { playlistTrackEntity ->
            playlistTrackConverter.map(playlistTrackEntity)
        }
        return trackModelList
    }

    override suspend fun getTrackIdByTrack(trackId: String): Int {
        return database.playlistTrackDao().getTrackIdByTrack(trackId)
    }

    override suspend fun addTrackToPlaylist(playlistId: Int, trackPlaylist: PlayListTrackModel) {
        val track = playlistTrackConverter.map(trackPlaylist)
        val allTrack = database.playlistTrackDao().getTracksFromPlaylist()
        val containsTrackInDB = allTrack.any { it.trackId == track.trackId }
        if (!containsTrackInDB) {
            database.playlistTrackDao().insert(track)
        }
        val trackId = database.playlistTrackDao().getTrackIdByTrack(trackPlaylist.trackId)
        database.playlistAndTrackRefDao().insertPlaylistAndTrackRef(PlaylistAndTrackRefEntity(playlistId, trackId))
        val playlist = database.playlistTrackDao().getTracksInPlaylist(playlistId)
        val newTrackCount = playlist.size
        if (newTrackCount != null) {
            database.playlistsDao().updateTrackCount(playlistId, newTrackCount)
        }
    }

    override suspend fun getSavedPlaylists(): Flow<List<PlaylistModel>> {
        return database.playlistsDao().getSavedPlaylists().map { convertFromTrackEntity(it) }
    }

    override suspend fun getSavedPlaylist(playlistId: Int): Flow<PlaylistModel> {
        return database.playlistsDao().getSavedPlaylist(playlistId)
            .map { playlistEntity ->
                playlistConverter.map(playlistEntity)
            }
    }

    override suspend fun getSavedTrackInPlaylist(): List<PlayListTrackModel> {
        val playlistTrackEntities = database.playlistTrackDao().getTracksFromPlaylist()
        return playlistTrackEntities.map { playlistTrackConverter.map(it) }
    }

    private fun convertFromTrackEntity(playlists: List<PlaylistEntity>): List<PlaylistModel> {
        return playlists.map { playlistConverter.map(it) }
    }

    private fun  convertFromPlaylistEntity(playlists: List<PlaylistModel>): List<PlaylistEntity> {
        return playlists.map { playlistConverter.map(it) }
    }


    override suspend fun isPlaylistEmpty(playlist: PlaylistModel, trackPlaylist: PlayListTrackModel): Boolean {
        val playlistId = playlistConverter.map(playlist).id
        val tracksInPlaylist = getTracksInPlaylist(playlistId)
        val foundTrack = tracksInPlaylist.find { it.trackId == trackPlaylist.trackId }
        return foundTrack == null
    }

}