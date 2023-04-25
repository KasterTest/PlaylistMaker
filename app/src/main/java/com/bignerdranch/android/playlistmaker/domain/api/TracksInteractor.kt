package com.bignerdranch.android.playlistmaker.domain.api

import com.bignerdranch.android.playlistmaker.domain.models.Track

interface TracksInteractor {
    fun likeTrack(trackId: String, consumer: TrackInfoConsumer)
    fun unlikeTrack(trackId: String, consumer: TrackInfoConsumer)
    fun getTrackInfo(trackId: String, consumer: TrackInfoConsumer)

    interface TrackInfoConsumer {
        fun consume(track: Track)
    }
}