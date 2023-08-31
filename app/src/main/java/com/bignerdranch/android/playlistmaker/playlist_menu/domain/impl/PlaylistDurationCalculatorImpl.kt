package com.bignerdranch.android.playlistmaker.playlist_menu.domain.impl

import com.bignerdranch.android.playlistmaker.playlist_menu.domain.api.PlaylistDurationCalculator
import com.bignerdranch.android.playlistmaker.search.domain.models.TrackModel
import java.text.SimpleDateFormat
import java.util.Locale

class PlaylistDurationCalculatorImpl : PlaylistDurationCalculator {
    override fun getTracksDuration(trackList: List<TrackModel>): Int {
        val sumStrangeAppleTime = trackList.sumOf {
            SimpleDateFormat("mm", Locale.getDefault()).format(it.trackTimeMillis).toInt()
        }
        return sumStrangeAppleTime

    }
}