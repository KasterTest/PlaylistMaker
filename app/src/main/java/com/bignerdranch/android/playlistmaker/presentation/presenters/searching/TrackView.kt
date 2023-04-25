package com.bignerdranch.android.playlistmaker.presentation.presenters.searching

import com.bignerdranch.android.playlistmaker.domain.models.Track

interface TrackView {
    fun updateTrackLiked(liked: Boolean)
    fun drawTrack(track: Track)
}