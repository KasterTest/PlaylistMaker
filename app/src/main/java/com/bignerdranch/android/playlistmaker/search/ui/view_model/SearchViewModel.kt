package com.bignerdranch.android.playlistmaker.search.ui.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.bignerdranch.android.playlistmaker.application.App
import com.bignerdranch.android.playlistmaker.creator.Creator
import com.bignerdranch.android.playlistmaker.search.data.storage.TrackSearchState
import com.bignerdranch.android.playlistmaker.search.domain.impl.SearchInteractor
import com.bignerdranch.android.playlistmaker.search.domain.models.TrackModel

class SearchViewModel(private val searchInteractor: SearchInteractor) : ViewModel() {

    private val historyList = ArrayList<TrackModel>()

    private val _trackSearchState = MutableLiveData<TrackSearchState>()
    val trackSearchState: LiveData<TrackSearchState>
        get() = _trackSearchState

    fun searchTracks(term: String) {
        _trackSearchState.value = TrackSearchState.Loading

        searchInteractor.searchTracks(term) { state ->
            _trackSearchState.postValue(state)
        }
    }

    init {
        historyList.addAll(searchInteractor.getSearchHistoryList())
        _trackSearchState.postValue(TrackSearchState.StateVisibleHistory(historyList))
    }

    fun getSearchHistoryList() {
        historyList.clear()
        historyList.addAll(searchInteractor.getSearchHistoryList())
        _trackSearchState.postValue(TrackSearchState.StateVisibleHistory(historyList))

    }

    fun clearSearchHistory() {
        historyList.clear()
        searchInteractor.clearSearchHistory()
        _trackSearchState.postValue(TrackSearchState.StateVisibleHistory(historyList))
    }

    fun addTrackToSearchHistory(track: TrackModel) {
        searchInteractor.addTrackToSearchHistory(track)
    }

    companion object {
        fun getViewModelFactory(): ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = this[APPLICATION_KEY] as App
                SearchViewModel(
                    searchInteractor = Creator.provideSearchInteractor(context = application)
                )
            }
        }
    }
}
