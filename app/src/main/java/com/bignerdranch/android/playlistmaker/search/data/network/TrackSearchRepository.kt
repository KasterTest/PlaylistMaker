package com.bignerdranch.android.playlistmaker.search.data.network

import com.bignerdranch.android.playlistmaker.search.data.storage.TrackSearchState
import com.bignerdranch.android.playlistmaker.search.domain.api.TrackSearchInteractor
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class TrackSearchRepository() : TrackSearchInteractor {

    private val itunesBaseUrl = "https://itunes.apple.com"

    private val retrofit = Retrofit.Builder()
        .baseUrl(itunesBaseUrl)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val itunesService = retrofit.create(ItunesApiService::class.java)

    private fun doRequest(term: String, callback: (TrackSearchState) -> Unit) {
        itunesService.searchTracks(term).enqueue(object : Callback<TrackSearchResponse> {
            override fun onResponse(call: Call<TrackSearchResponse>, response: Response<TrackSearchResponse>) {
                response.body()?.let {
                    if (it.results.isEmpty()) {
                        callback(TrackSearchState.NotFound)
                    } else {
                        callback(TrackSearchState.Success(it.results))
                    }
                } ?: run {
                    callback(TrackSearchState.Error("Ошибка"))
                }
            }

            override fun onFailure(call: Call<TrackSearchResponse>, t: Throwable) {
                callback(TrackSearchState.Error(t.message ?: "Ошибка"))
            }
        })
    }

    override fun searchTracks(term: String, callback: (TrackSearchState) -> Unit) {
        doRequest(term, callback)
    }
}