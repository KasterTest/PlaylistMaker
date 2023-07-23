package com.bignerdranch.android.playlistmaker.medialibrary.ui.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bignerdranch.android.playlistmaker.medialibrary.domain.db.FavoriteTracksInteractor
import com.bignerdranch.android.playlistmaker.medialibrary.domain.models.FavoriteState
import com.bignerdranch.android.playlistmaker.search.domain.models.TrackModel
import kotlinx.coroutines.launch

class FavoriteTracksViewModel(
    private val favoriteTracksInteractor: FavoriteTracksInteractor) : ViewModel() {


    private val contentStateLiveData = MutableLiveData<FavoriteState>()
    fun observeContentState(): LiveData<FavoriteState> = contentStateLiveData

    fun fillData() {
        viewModelScope.launch {
            favoriteTracksInteractor
                .getFavoriteTracks()
                .collect { trackList ->
                    processResult(trackList)
                }
        }
    }

    private fun processResult(tracks: List<TrackModel>) {

        when {
            tracks.isEmpty() -> {
                contentStateLiveData.value = FavoriteState.Empty
            }

            else -> {
                contentStateLiveData.value = FavoriteState.FavoriteTracks(tracks)
            }
        }
    }
}