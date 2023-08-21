package com.bignerdranch.android.playlistmaker.medialibrary.domain.models

import com.bignerdranch.android.playlistmaker.search.domain.models.TrackModel


class PlaylistTrackModelConverter {
    fun map(playlistTrack: PlayListTrackModel): TrackModel {
        return with(playlistTrack) {
            TrackModel(
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

    fun map(playlistTrack: TrackModel): PlayListTrackModel {
        return with(playlistTrack) {
            PlayListTrackModel(
                id = 0,
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