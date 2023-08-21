package com.bignerdranch.android.playlistmaker.playlist_menu.domain.impl

import com.bignerdranch.android.playlistmaker.playlist_menu.domain.api.PlaylistDurationCalculator
import com.bignerdranch.android.playlistmaker.search.domain.models.TrackModel

class PlaylistDurationCalculatorImpl : PlaylistDurationCalculator {
    override fun getTracksDuration(trackList: List<TrackModel>): Int {

        return trackList.sumOf { it.trackTimeMillis } / MILLIS_IN_MINUTES
    }

    companion object {
        private const val MILLIS_IN_MINUTES = 60000
    }
}