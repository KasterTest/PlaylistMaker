package com.bignerdranch.android.playlistmaker.playlist_menu.domain.api

import com.bignerdranch.android.playlistmaker.search.domain.models.TrackModel

interface PlaylistDurationCalculator {

    fun getTracksDuration(trackList: List<TrackModel>): Int
}