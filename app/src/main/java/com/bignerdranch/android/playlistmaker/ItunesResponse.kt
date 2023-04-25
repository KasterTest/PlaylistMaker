package com.bignerdranch.android.playlistmaker

import com.bignerdranch.android.playlistmaker.domain.models.Track

data class ItunesResponse (val resultCount: Int,
                           val results: List<Track>)

