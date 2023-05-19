package com.bignerdranch.android.playlistmaker.utils.router

import android.app.Activity
import android.content.Intent
import com.bignerdranch.android.playlistmaker.player.ui.activity.PlayerActivity
import com.bignerdranch.android.playlistmaker.search.domain.models.TrackModel
import com.google.gson.Gson

class NavigationRouter(
    var activity: Activity?
) {

    fun openAudioPlayer(track: TrackModel) {
        val intent = Intent(activity, PlayerActivity::class.java)
        intent.putExtra(TRACK_IN_PLAYER, Gson().toJson(track))
        activity?.startActivity(intent)
    }

    fun getTrackInfo(): TrackModel {
        return Gson().fromJson((activity?.intent?.getStringExtra(TRACK_IN_PLAYER)), TrackModel::class.java)
    }

    fun goBack() {
        activity?.finish()
    }

    companion object {
        private const val TRACK_IN_PLAYER = "track_model"
    }
}