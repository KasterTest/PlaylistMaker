package com.bignerdranch.android.playlistmaker.domain.impl

import com.bignerdranch.android.playlistmaker.domain.api.TracksInteractor
import com.bignerdranch.android.playlistmaker.domain.api.TracksRepository
import java.util.concurrent.Executors

class TracksInteractorImpl(private val repository: TracksRepository) : TracksInteractor {
    // Используется для асинхронного запроса в другом потоке, чтобы освободить главный поток
    private val executor = Executors.newCachedThreadPool()

    override fun likeTrack(trackId: String, consumer: TracksInteractor.TrackInfoConsumer) {
        executor.execute {
            repository.likeTrack(trackId)
            consumer.consume(repository.getTrackForId(trackId))
        }
    }

    override fun unlikeTrack(trackId: String, consumer: TracksInteractor.TrackInfoConsumer) {
        executor.execute {
            repository.unlikeTrack(trackId)
            consumer.consume(repository.getTrackForId(trackId))
        }
    }

    override fun getTrackInfo(trackId: String, consumer: TracksInteractor.TrackInfoConsumer) {
        executor.execute {
            consumer.consume(repository.getTrackForId(trackId))
        }
    }
}