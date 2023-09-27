package com.bignerdranch.android.playlistmaker.medialibrary.data.db.dao

import androidx.room.*
import com.bignerdranch.android.playlistmaker.medialibrary.data.db.entity.PlaylistTrackEntity


@Dao
interface PlaylistTrackDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(track: PlaylistTrackEntity)

    @Query("SELECT * FROM playlist_tracks;")
    suspend fun getTracksFromPlaylist(): List<PlaylistTrackEntity>

    @Query("SELECT * FROM playlist_tracks WHERE trackId = :trackId")
    suspend fun getTrackByTrackId(trackId: String): PlaylistTrackEntity?

    @Query("SELECT id FROM playlist_tracks WHERE trackId = :trackId")
    suspend fun getTrackIdByTrack(trackId: String): Int

    @Query("SELECT * FROM playlist_tracks WHERE id IN (SELECT trackId FROM playlist_and_tracks WHERE playlistId = :playlistId)")
    suspend fun getTracksInPlaylist(playlistId: Int): List<PlaylistTrackEntity>

    @Query("DELETE FROM playlist_tracks WHERE trackId = :trackId")
    suspend fun deleteTrackByTrackId(trackId: String)

}

