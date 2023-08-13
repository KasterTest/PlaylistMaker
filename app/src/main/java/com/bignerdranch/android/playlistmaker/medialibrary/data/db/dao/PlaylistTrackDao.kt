package com.bignerdranch.android.playlistmaker.medialibrary.data.db.dao

import androidx.room.*
import com.bignerdranch.android.playlistmaker.medialibrary.data.db.entity.PlaylistTrackEntity


@Dao
interface PlaylistTrackDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(track: PlaylistTrackEntity)

    @Query("UPDATE playlist_tracks SET playlist_ids = :newPlaylistId WHERE track = :track")
    suspend fun updatePlaylistTrack(newPlaylistId: String, track: String)

    @Query("SELECT * FROM playlist_tracks;")
    suspend fun getTracksFromPlaylist(): List<PlaylistTrackEntity>
}

