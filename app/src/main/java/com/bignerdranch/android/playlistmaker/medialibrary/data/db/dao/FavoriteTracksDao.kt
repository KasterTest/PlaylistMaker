package com.bignerdranch.android.playlistmaker.medialibrary.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.bignerdranch.android.playlistmaker.medialibrary.data.db.entity.TrackEntity

@Dao
interface  FavoriteTracksDao {

    @Insert
    suspend fun insertTrack(track: TrackEntity)

    @Query("DELETE FROM favorite_tracks WHERE trackId = :trackId")
    suspend fun deleteTrack(trackId: String)

    @Query("SELECT * FROM favorite_tracks ORDER BY id DESC")
    suspend fun getAllFavoriteTracks(): List<TrackEntity>

    @Query("SELECT trackId FROM favorite_tracks")
    suspend fun getAllFavoriteTrackIds(): List<String>

}
