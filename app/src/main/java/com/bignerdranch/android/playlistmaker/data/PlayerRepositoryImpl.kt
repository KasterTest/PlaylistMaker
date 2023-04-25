package com.bignerdranch.android.playlistmaker.data

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.media.MediaPlayer

import com.bignerdranch.android.playlistmaker.SearchActivity
import com.bignerdranch.android.playlistmaker.SearchActivity.Companion.KEY_TRACK
import com.bignerdranch.android.playlistmaker.domain.PlayerRepository
import com.bignerdranch.android.playlistmaker.domain.models.Track
import com.google.gson.Gson

class PlayerRepositoryImpl (context: Context) : PlayerRepository {

    private var mediaPlayer: MediaPlayer = MediaPlayer()
    private var isPrepared = false
    private val trackInPlayer =
        context.getSharedPreferences(SearchActivity.TRACK_IN_PLAYER, MODE_PRIVATE)


    override fun preparePlayer() {
        isPrepared = true
        mediaPlayer.setDataSource(getTrack().previewUrl)
        mediaPlayer.prepareAsync()
    }

    override fun startPlayer() {
        if (isPrepared) {
            mediaPlayer.start()
        } else {
            preparePlayer()
            mediaPlayer.setOnPreparedListener {
                mediaPlayer.start()
            }
        }
    }
    override fun stopPlayer() {
        mediaPlayer.stop()
    }

    override fun pausePlayer() {
        mediaPlayer.pause()
    }

    override fun releasePlayer() {
        mediaPlayer.release()
    }

    override fun updatePlayingTime(): Int {
        return mediaPlayer.currentPosition }


    override fun getTrack(): Track {
        val gson = Gson()
        val serializerTrack = trackInPlayer.getString(KEY_TRACK, "") ?: ""
        return gson.fromJson(serializerTrack, Track::class.java)
    }

    override fun setOnPreparedListener(listener: (() -> Unit)?) {
        mediaPlayer.setOnPreparedListener{ listener?.invoke()}
    }

    override fun setOnCompletionListener(listener: (() -> Unit)?) {
        mediaPlayer.setOnCompletionListener { listener?.invoke()}
    }

}