package com.bignerdranch.android.playlistmaker.player.domain.impl

import com.bignerdranch.android.playlistmaker.player.domain.api.PlayerRepository
import com.bignerdranch.android.playlistmaker.player.domain.models.PlayerState

class PlayerInteractor(private val playerRepository: PlayerRepository) {


    fun startPlayer() {
        playerRepository.startPlayer()
    }

    fun stopPlayer() {
        playerRepository.stopPlayer()
    }

    fun pausePlayer() {
        playerRepository.pausePlayer()
    }

    fun releasePlayer() {
        playerRepository.releasePlayer()
    }

    fun updatePlayingTime(): Int {
        return playerRepository.updatePlayingTime()
    }

//    fun setOnPreparedListener(listener: (() -> Unit)?) {
//        playerRepository.setOnPreparedListener(listener)
//    }

    fun setOnCompletionListener(listener: (() -> Unit)?) {
      playerRepository.setOnCompletionListener(listener)
    }

    fun getPlayerState(): PlayerState {
        return playerRepository.playerState
    }
}



