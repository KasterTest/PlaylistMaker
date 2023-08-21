package com.bignerdranch.android.playlistmaker.medialibrary.data.db.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "playlist_tracks")
data class PlaylistTrackEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val trackId: String,
    val trackName: String,
    val artistName: String,
    @ColumnInfo(name = "track_duration")
    val trackTimeMillis: Int,
    val collectionName: String,
    val releaseDate: String,
    @ColumnInfo(name = "genre")
    val primaryGenreName: String,
    val country: String,
    @ColumnInfo(name = "cover_url")
    val artworkUrl100: String,
    val previewUrl: String
)