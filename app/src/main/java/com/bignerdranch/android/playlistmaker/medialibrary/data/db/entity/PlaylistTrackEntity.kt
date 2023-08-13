package com.bignerdranch.android.playlistmaker.medialibrary.data.db.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "playlist_tracks")
data class PlaylistTrackEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    @ColumnInfo(name = "playlist_ids")
    val playlistId: String,
    @ColumnInfo(name = "track")
    val track: String
)