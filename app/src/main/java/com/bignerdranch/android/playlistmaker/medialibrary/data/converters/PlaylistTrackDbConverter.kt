package com.bignerdranch.android.playlistmaker.medialibrary.data.converters

import com.bignerdranch.android.playlistmaker.medialibrary.data.db.entity.PlaylistTrackEntity
import com.bignerdranch.android.playlistmaker.medialibrary.domain.models.PlayListTrackModel


class PlaylistTrackDbConverter {

    fun map(playlistTrack: PlayListTrackModel): PlaylistTrackEntity {
        return with(playlistTrack) {
            PlaylistTrackEntity(
                id = id,
                trackId = trackId,
                trackName = trackName,
                artistName = artistName,
                trackTimeMillis = trackTimeMillis,
                collectionName = collectionName,
                releaseDate = releaseDate,
                primaryGenreName = primaryGenreName,
                country = country,
                artworkUrl100 = artworkUrl100,
                previewUrl = previewUrl
            )
        }
    }

    fun map(playlistTrack: PlaylistTrackEntity): PlayListTrackModel {
        return with(playlistTrack) {
            PlayListTrackModel(
                id = id,
                trackId = trackId,
                trackName = trackName,
                artistName = artistName,
                trackTimeMillis = trackTimeMillis,
                collectionName = collectionName,
                releaseDate = releaseDate,
                primaryGenreName = primaryGenreName,
                country = country,
                artworkUrl100 = artworkUrl100,
                previewUrl = previewUrl

            )
        }
    }
}

