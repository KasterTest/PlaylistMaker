package com.bignerdranch.android.playlistmaker.search.ui.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bignerdranch.android.playlistmaker.search.data.storage.TrackSearchState
import com.bignerdranch.android.playlistmaker.search.domain.impl.SearchInteractor
import com.bignerdranch.android.playlistmaker.search.domain.models.TrackModel
import kotlinx.coroutines.launch

class SearchViewModel(private val searchInteractor: SearchInteractor) : ViewModel() {

    private val historyList = ArrayList<TrackModel>()

    private val _trackSearchState = MutableLiveData<TrackSearchState>()
    val trackSearchState: LiveData<TrackSearchState>
        get() = _trackSearchState

    fun searchTracks(term: String) {
        viewModelScope.launch {
            _trackSearchState.value = TrackSearchState.Loading

            searchInteractor.searchTracks(term).collect { state ->
                _trackSearchState.value = state
            }
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


}
