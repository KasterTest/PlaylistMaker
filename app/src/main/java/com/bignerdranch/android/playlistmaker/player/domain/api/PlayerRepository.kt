package com.bignerdranch.android.playlistmaker.player.domain.api
import com.bignerdranch.android.playlistmaker.player.domain.models.PlayerState


interface PlayerRepository {

    fun startPlayer()

    fun stopPlayer()

    fun pausePlayer()

    fun updatePlayingTime () : Int

    fun setOnPreparedListener(listener: (() -> Unit)?)

    fun setOnCompletionListener(listener: (() -> Unit)?)

    var playerState: PlayerState

}