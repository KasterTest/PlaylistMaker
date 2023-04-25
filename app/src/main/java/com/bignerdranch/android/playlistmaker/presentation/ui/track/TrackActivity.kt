package com.bignerdranch.android.playlistmaker.presentation.ui.track

//import android.app.Activity
//import com.bignerdranch.android.playlistmaker.creator.Creator
//import com.bignerdranch.android.playlistmaker.domain.models.Track
//import com.bignerdranch.android.playlistmaker.presentation.presenters.searching.TrackView

//class TrackActivity : Activity(), TrackView {
//
//    // Activity specific code
//
//    private val presenter = Creator.providePresenter(
//        view = this,
//        trackId = intent.extras?.getString("TRACK_KEY", "") ?: "",
//    )
//
//    override fun updateTrackLiked(liked: Boolean) {
//        // update layout
//    }
//
//    override fun drawTrack(track: Track) {
//        // draw track
//    }
//
//    override fun onDestroy() {
//        super.onDestroy()
//        presenter.onDestroyed()
//    }
//}