package com.bignerdranch.android.playlistmaker

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query


interface ItunesSearchApi {
    @GET("search/")
    fun findTrack(@Query("term") trackOrArtist: String): Call<ItunesResponse>
}