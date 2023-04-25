package com.bignerdranch.android.playlistmaker.domain.impl

import com.bignerdranch.android.playlistmaker.domain.PlayerRepository
import com.bignerdranch.android.playlistmaker.domain.models.Track

class PlayerInteractor(private val playerRepository: PlayerRepository) {

    fun preparePlayer() {
        playerRepository.preparePlayer()
    }

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

    fun getTrack(): Track? {
        return playerRepository.getTrack()
    }

    fun setOnPreparedListener(listener: (() -> Unit)?) {
        playerRepository.setOnPreparedListener(listener)
    }


    fun setOnCompletionListener(listener: (() -> Unit)?) {
      playerRepository.setOnCompletionListener(listener)
    }
}



