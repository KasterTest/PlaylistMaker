package com.bignerdranch.android.playlistmaker.medialibrary.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.bignerdranch.android.playlistmaker.medialibrary.data.db.entity.PlaylistAndTrackRefEntity

@Dao
interface PlaylistAndTrackRefDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPlaylistAndTrackRef(playlistAndTrackRefEntity: PlaylistAndTrackRefEntity)

    @Query("DELETE FROM playlist_and_tracks WHERE playlistId = :playlistId")
    suspend fun deleteAllTracksForPlaylist(playlistId: Int)

    @Query("DELETE FROM playlist_and_tracks WHERE trackId = :trackId AND playlistId = :playlistId")
    suspend fun deleteTrackFromTrackPlaylist(playlistId: Int, trackId: Int)

    @Query("SELECT * FROM playlist_and_tracks")
    suspend fun getPlaylistAndTracksRef(): List<PlaylistAndTrackRefEntity>


}