package com.bignerdranch.android.playlistmaker.domain
import com.bignerdranch.android.playlistmaker.domain.models.Track


interface PlayerRepository {


    fun preparePlayer()

    fun startPlayer()

    fun stopPlayer()

    fun pausePlayer()

    fun releasePlayer()

    fun updatePlayingTime () : Int

    fun getTrack () : Track?

    fun setOnPreparedListener(listener: (() -> Unit)?)

    fun setOnCompletionListener(listener: (() -> Unit)?)

}