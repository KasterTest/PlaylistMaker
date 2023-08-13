package com.bignerdranch.android.playlistmaker.medialibrary.domain.models


import com.bignerdranch.android.playlistmaker.search.domain.models.TrackModel

class PlayListTrackModel(
    val id: Int,
    val playlistId: MutableList<Int?>,
    var track: TrackModel
    )