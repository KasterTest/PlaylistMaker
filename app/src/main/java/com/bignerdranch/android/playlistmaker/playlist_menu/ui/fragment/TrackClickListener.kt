package com.bignerdranch.android.playlistmaker.playlist_menu.ui.fragment

import com.bignerdranch.android.playlistmaker.search.domain.models.TrackModel

interface TrackClickListener {
    fun onTrackClick(track: TrackModel)
    fun onTrackLongClick(track: TrackModel)
}

