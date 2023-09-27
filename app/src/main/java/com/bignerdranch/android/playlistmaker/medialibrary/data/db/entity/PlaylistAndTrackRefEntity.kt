package com.bignerdranch.android.playlistmaker.medialibrary.data.db.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index


@Entity(
    tableName = "playlist_and_tracks",
    primaryKeys = ["playlistId", "trackId"],
    foreignKeys = [
        ForeignKey(
            entity = PlaylistEntity::class,
            parentColumns = ["id"],
            childColumns = ["playlistId"]
        ),
        ForeignKey(
            entity = PlaylistTrackEntity::class,
            parentColumns = ["id"],
            childColumns = ["trackId"]
        )
    ],
    indices = [Index("trackId")]
)
data class PlaylistAndTrackRefEntity(
    val playlistId: Int,
    val trackId: Int
)