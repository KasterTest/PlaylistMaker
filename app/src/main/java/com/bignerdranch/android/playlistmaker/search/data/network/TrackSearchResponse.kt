package com.bignerdranch.android.playlistmaker.search.data.network

import com.bignerdranch.android.playlistmaker.search.domain.models.TrackModel

data class TrackSearchResponse(val resultCount: Int, val results: List<TrackModel>)


