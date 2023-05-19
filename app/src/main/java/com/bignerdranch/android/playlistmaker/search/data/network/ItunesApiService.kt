package com.bignerdranch.android.playlistmaker.search.data.network

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ItunesApiService {
    @GET("search/")
    fun searchTracks(@Query("term") term: String): Call<TrackSearchResponse>
}