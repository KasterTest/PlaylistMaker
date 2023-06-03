package com.bignerdranch.android.playlistmaker.player.data

import android.media.MediaPlayer

import com.bignerdranch.android.playlistmaker.player.domain.api.PlayerRepository
import com.bignerdranch.android.playlistmaker.player.domain.models.PlayerState

class PlayerRepositoryImpl (private val url: String) : PlayerRepository {


    private var mediaPlayer: MediaPlayer = MediaPlayer()
    override var playerState = PlayerState.NOT_PREPARED

    init {
        mediaPlayer.apply {
            println(url)
            setDataSource(url)
            mediaPlayer.prepareAsync()
            setOnPreparedListener {
                playerState = PlayerState.READY
            }
            setOnCompletionListener {
                playerState = PlayerState.READY
            }
        }
    }

    override fun startPlayer() {
        if (playerState == PlayerState.NOT_PREPARED){
            return
        }
        mediaPlayer.start()
        playerState = PlayerState.PLAYING

    }
    override fun stopPlayer() {
        mediaPlayer.stop()
    }

    override fun pausePlayer() {
        mediaPlayer.pause()
        playerState = PlayerState.PAUSED
    }

    override fun releasePlayer() {
        mediaPlayer.release()
    }

    override fun updatePlayingTime(): Int {
        return mediaPlayer.currentPosition
    }

    override fun setOnPreparedListener(listener: (() -> Unit)?) {
        mediaPlayer.setOnPreparedListener{ listener?.invoke()}
    }

    override fun setOnCompletionListener(listener: (() -> Unit)?) {
        mediaPlayer.setOnCompletionListener { listener?.invoke()}
    }

}