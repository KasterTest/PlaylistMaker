//package com.bignerdranch.android.playlistmaker.creator
//import com.bignerdranch.android.playlistmaker.data.network.NetworkClientImpl
//import com.bignerdranch.android.playlistmaker.domain.impl.TracksInteractorImpl
//import com.bignerdranch.android.playlistmaker.presentation.presenters.searching.TrackPresenter
//import com.bignerdranch.android.playlistmaker.presentation.presenters.searching.TrackView
//
//object Creator {
//    private fun getRepository(): TracksRepositoryImpl {
//        return TracksRepositoryImpl(NetworkClientImpl())
//    }
//
//    fun providePresenter(view: TrackView, trackId: String): TrackPresenter {
//        return TrackPresenter(
//            view = view,
//            trackId = trackId,
//            tracksInteractor = TracksInteractorImpl(getRepository()),
//        )
//    }
//
//    private fun getTrackInteractor(): TrackInteractor {
//        if (Config.isAnonymousVersion) {
//            // Если пользователь не залогинен в приложение, то ему будет попадаться реклама и функции будут частично ограничены
//            TrackInteractorAnonymousImpl(anonymousDependencies())
//        } else {
//            // Если пользователь залогинен, то у него есть доступ ко всем функциям
//            TrackInteractorLoginImpl(loginDependencies())
//        }
//    }
//}