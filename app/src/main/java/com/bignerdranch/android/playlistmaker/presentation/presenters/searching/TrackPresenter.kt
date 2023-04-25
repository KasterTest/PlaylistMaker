package com.bignerdranch.android.playlistmaker.presentation.presenters.searching
import com.bignerdranch.android.playlistmaker.domain.api.TracksInteractor
import com.bignerdranch.android.playlistmaker.domain.models.Track

class TrackPresenter(
    private var view: TrackView?,
    private val trackId: String,
    private val tracksInteractor: TracksInteractor,
) {
    private val trackConsumer: TracksInteractor.TrackInfoConsumer =
        object : TracksInteractor.TrackInfoConsumer {
            override fun consume(track: Track) {
                view?.drawTrack(track)
            }
        }

    init {
        tracksInteractor.getTrackInfo(trackId, trackConsumer)
    }

    fun likeTrack() {
        tracksInteractor.likeTrack(trackId, trackConsumer)
    }

    fun unlikeTrack() {
        tracksInteractor.unlikeTrack(trackId, trackConsumer)
    }

    fun onViewDestroyed() {
        view = null
    }
}