package com.bignerdranch.android.playlistmaker.playlist_menu.ui.fragment

import android.content.Context
import com.bignerdranch.android.playlistmaker.R
import com.bignerdranch.android.playlistmaker.playlist_creator.domain.models.PlaylistModel
import com.bignerdranch.android.playlistmaker.search.domain.models.TrackModel
import com.bignerdranch.android.playlistmaker.utils.millisConverter

class MessageCreator(private val context: Context) {

    fun create(playlist: PlaylistModel, trackModel: List<TrackModel>): String {
        val message = StringBuilder()
        message.append("${context.getString(R.string.playlist)}: ")
        message.append("'${playlist.playlistName}'\n")
        if (playlist.playlistDescription.isNotEmpty()) {
            message.append("${playlist.playlistDescription}\n")
        }
        message.append(
            "${
                context.resources.getQuantityString(
                    R.plurals.tracks, playlist.tracksCount, playlist.tracksCount
                )
            }\n\n"
        )
        trackModel.forEachIndexed { index, track ->
            message.append("${index + 1}. " + "${track.artistName} - ${track.trackName} (${track.trackTimeMillis.millisConverter()})\n")
        }
        return message.toString()
    }
}