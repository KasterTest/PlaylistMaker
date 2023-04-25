package com.bignerdranch.android.playlistmaker.data

interface NetworkClient {
    fun doRequest(dto: Any): Any
}