package com.bignerdranch.android.playlistmaker.presentation.presenters.player


import android.os.Handler
import com.bignerdranch.android.playlistmaker.presentation.param.PlayerParam
import com.bignerdranch.android.playlistmaker.presentation.param.PlayerState
import com.bignerdranch.android.playlistmaker.domain.impl.PlayerInteractor
import com.bignerdranch.android.playlistmaker.domain.models.Track



class PlayerPresenter(
    private val playerInteractor: PlayerInteractor,
    private val view: PlayerView,
    private val handler: Handler
) {
    private var playerState = PlayerState.DEFAULT

    private val updatePlayingTimeRunnable = object : Runnable {
        override fun run() {
            view.setCurrentTime()
            handler.postDelayed(this, PlayerParam.UPDATE_DEBOUNCE_DELAY)
        }
    }

    fun preparePlayer() {
        playerInteractor.preparePlayer()
        playerInteractor.setOnPreparedListener {
            playerState = PlayerState.PREPARED
        }
        setCompletionPlayerListener()
    }

    fun getTrack() : Track? {
        return playerInteractor.getTrack()
    }

    private fun setCompletionPlayerListener() {
        playerInteractor.setOnCompletionListener {
            view.setButtonToPlay()
            view.setStartTime()
            playerState = PlayerState.PREPARED
            handler.removeCallbacks(updatePlayingTimeRunnable)
        }
    }

    private fun startPlayer() {
        playerInteractor.startPlayer()
        handler.post(updatePlayingTimeRunnable)
        view.setButtonToPause()
        playerState = PlayerState.PLAYING
    }

    fun pausePlayer() {
        playerInteractor.pausePlayer()
        view.setButtonToPlay()
        playerState = PlayerState.PAUSED
        handler.removeCallbacks(updatePlayingTimeRunnable)
    }

    fun playbackControl() {
        when (playerState) {
            PlayerState.PLAYING -> {
                pausePlayer()
            }
            PlayerState.PREPARED, PlayerState.PAUSED -> {
                startPlayer()
            }
            PlayerState.DEFAULT ->  pausePlayer()
        }
    }

    fun playerRelease() {
        playerInteractor.releasePlayer()
        handler.removeCallbacks(updatePlayingTimeRunnable)
    }

    fun playerStop() {
        playerInteractor.stopPlayer()
        handler.removeCallbacks(updatePlayingTimeRunnable)
    }

    fun getCurrentPosition(): Int {
        return playerInteractor.updatePlayingTime()
    }

}