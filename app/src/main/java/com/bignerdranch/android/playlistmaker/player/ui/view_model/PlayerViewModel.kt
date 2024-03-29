package com.bignerdranch.android.playlistmaker.player.ui.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bignerdranch.android.playlistmaker.medialibrary.domain.db.FavoriteTracksInteractor
import com.bignerdranch.android.playlistmaker.player.domain.impl.PlayerInteractor
import com.bignerdranch.android.playlistmaker.player.domain.models.PlayerState
import com.bignerdranch.android.playlistmaker.player.ui.models.PlayerActivityState
import com.bignerdranch.android.playlistmaker.search.domain.models.TrackModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


class PlayerViewModel(private val playerInteractor: PlayerInteractor,
                      private val favoriteTrackInteractor: FavoriteTracksInteractor) : ViewModel() {

    private var timerJob: Job? = null

    init {
        setCompletionPlayerListener()
    }

    private val _state = MutableLiveData<PlayerActivityState>()
    val state: LiveData<PlayerActivityState> = _state

    override fun onCleared() {
        super.onCleared()
        playerInteractor.pausePlayer()
        playerInteractor.stopPlayer()
    }

    private fun setCompletionPlayerListener() {
        playerInteractor.setOnCompletionListener {
            _state.postValue(PlayerActivityState.StateOnComplitionTrack)
            timerJob?.cancel()
        }
    }

    private fun startPlayer() {
        playerInteractor.startPlayer()
        _state.postValue(PlayerActivityState.StatePlayerPlay)
        startTimer()
    }

    private fun startTimer() {
        timerJob = viewModelScope.launch {
            while (true) {
                delay(UPDATE_DEBOUNCE_DELAY)
                getCurrentPosition()
            }


        }
    }

    fun pausePlayer() {
        playerInteractor.pausePlayer()
        _state.postValue(PlayerActivityState.StatePlayerPause)
        timerJob?.cancel()
    }

    private fun getCurrentPosition() {
        _state.postValue(PlayerActivityState.StatePlayingPosition(playerInteractor.updatePlayingTime()))
    }

    fun playButtonClicked() {
        when (playerInteractor.getPlayerState()) {
            PlayerState.PLAYING -> pausePlayer()
            PlayerState.NOT_PREPARED -> return
            else -> startPlayer()
        }
    }

    fun buttonLikeClicked(track: TrackModel){
        if (track.isFavorite) {
            unLikeTrack(track)
        }
        else {
            likeTrack(track)
        }
    }

    fun likeTrack(track: TrackModel){
        _state.postValue(PlayerActivityState.StateTrackFavorite)
        viewModelScope.launch{
            favoriteTrackInteractor.addToFavoriteTracks(track)
        }
    }

    fun unLikeTrack(track: TrackModel) {
        _state.postValue(PlayerActivityState.StateTrackUnFavorite)
        viewModelScope.launch{
            favoriteTrackInteractor.removeFromFavoriteTracks(track)
        }
    }


    companion object {
        private const val UPDATE_DEBOUNCE_DELAY = 300L
    }
}
