package com.bignerdranch.android.playlistmaker.medialibrary.data.converters

import com.bignerdranch.android.playlistmaker.medialibrary.data.db.entity.TrackEntity
import com.bignerdranch.android.playlistmaker.search.domain.models.TrackModel

class TracksDbConvertor {


    fun map(track: TrackModel): TrackEntity {
        return TrackEntity(
            trackId = track.trackId,
            trackName = track.trackName,
            artistName = track.artistName,
            trackTimeMillis = track.trackTimeMillis,
            collectionName = track.collectionName,
            releaseDate = track.releaseDate,
            primaryGenreName = track.primaryGenreName,
            country = track.country,
            artworkUrl100 = track.artworkUrl100,
            previewUrl = track.previewUrl
        )
    }

    fun map(track: TrackEntity): TrackModel {
        return TrackModel(
            trackId = track.trackId,
            trackName = track.trackName,
            artistName = track.artistName,
            trackTimeMillis = track.trackTimeMillis,
            collectionName = track.collectionName,
            releaseDate = track.releaseDate,
            primaryGenreName = track.primaryGenreName,
            country = track.country,
            artworkUrl100 = track.artworkUrl100,
            previewUrl = track.previewUrl,
            isFavorite = false
        )
    }
}
