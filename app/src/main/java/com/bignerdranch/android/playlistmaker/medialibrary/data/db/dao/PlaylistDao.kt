package com.bignerdranch.android.playlistmaker.medialibrary.data.db.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.bignerdranch.android.playlistmaker.medialibrary.data.db.entity.PlaylistEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface PlaylistDao {
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPlaylist(playlist: PlaylistEntity)
    
    @Delete
    suspend fun deletePlaylist(playlist: PlaylistEntity)

    @Query("SELECT * FROM playlists ORDER BY saveDate DESC;")
    fun getSavedPlaylists(): Flow<List<PlaylistEntity>>

    @Query("SELECT * FROM playlists WHERE id = :playlistId")
    fun getSavedPlaylist(playlistId: Int): Flow<PlaylistEntity>

    @Query("UPDATE playlists SET countTracks = :newCount WHERE id = :playlistId")
    suspend fun updatePlaylistTrackCount(playlistId: Int, newCount: Int)

    @Query("UPDATE playlists SET countTracks = :newTrackCount WHERE id = :playlistId")
    suspend fun updateTrackCount(playlistId: Int, newTrackCount: Int)

    @Update
    suspend fun updatePlaylist(playlist: PlaylistEntity)


}