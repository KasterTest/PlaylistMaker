package com.bignerdranch.android.playlistmaker.domain.api

import com.bignerdranch.android.playlistmaker.domain.models.Track

interface TracksRepository {
    fun likeTrack(trackId: String)
    fun unlikeTrack(trackId: String)
    fun getTrackForId(trackId: String): Track
}