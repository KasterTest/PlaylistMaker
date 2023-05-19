package com.bignerdranch.android.playlistmaker.player.ui.view_model


import android.os.Handler
import android.os.Looper
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.bignerdranch.android.playlistmaker.creator.Creator
import com.bignerdranch.android.playlistmaker.player.domain.impl.PlayerInteractor
import com.bignerdranch.android.playlistmaker.player.domain.models.PlayerState
import com.bignerdranch.android.playlistmaker.player.ui.models.PlayerActivityState


class PlayerViewModel(private val playerInteractor: PlayerInteractor) : ViewModel() {

    private val handler: Handler = Handler(Looper.getMainLooper())

    init {
        setCompletionPlayerListener()
    }

    private val _state = MutableLiveData<PlayerActivityState>()
    val state: LiveData<PlayerActivityState> = _state

    override fun onCleared() {
        super.onCleared()
        playerInteractor.pausePlayer()
        playerInteractor.stopPlayer()
        handler.removeCallbacksAndMessages(null)
    }

    private val updatePlayingTimeRunnable = object : Runnable {
        override fun run() {
            getCurrentPosition()
            handler.postDelayed(this, UPDATE_DEBOUNCE_DELAY)
        }
    }

    private fun setCompletionPlayerListener() {
        playerInteractor.setOnCompletionListener {
            _state.postValue(PlayerActivityState.StateOnComplitionTrack)
            handler.removeCallbacks(updatePlayingTimeRunnable)
        }
    }

    private fun startPlayer() {
        playerInteractor.startPlayer()
        _state.postValue(PlayerActivityState.StatePlayerPlay)
        handler.postDelayed(updatePlayingTimeRunnable, UPDATE_DEBOUNCE_DELAY)
    }

    fun pausePlayer() {
        playerInteractor.pausePlayer()
        _state.postValue(PlayerActivityState.StatePlayerPause)
        handler.removeCallbacks(updatePlayingTimeRunnable)
    }

    private fun getCurrentPosition() {
        _state.postValue(PlayerActivityState.StatePlayingPosition(playerInteractor.updatePlayingTime()))
    }

    fun playerRelease() {
        playerInteractor.releasePlayer()
        handler.removeCallbacks(updatePlayingTimeRunnable)
    }

    fun playerStop() {
        playerInteractor.stopPlayer()
        handler.removeCallbacks(updatePlayingTimeRunnable)
    }

    fun playButtonClicked() {
        when (playerInteractor.getPlayerState()) {
            PlayerState.PLAYING -> pausePlayer()
            PlayerState.NOT_PREPARED -> return
            else -> startPlayer()
        }
    }

    companion object {
        private const val UPDATE_DEBOUNCE_DELAY = 500L

        fun getViewModelFactory(trackUrl: String): ViewModelProvider.Factory = viewModelFactory {
            initializer {
                PlayerViewModel(
                    playerInteractor = Creator.provideMediaInteractor(trackUrl)
                )
            }
        }
    }
}
