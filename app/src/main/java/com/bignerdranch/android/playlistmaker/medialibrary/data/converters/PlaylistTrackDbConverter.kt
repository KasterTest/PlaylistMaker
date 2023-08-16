package com.bignerdranch.android.playlistmaker.medialibrary.data.converters

import com.bignerdranch.android.playlistmaker.medialibrary.data.db.entity.PlaylistTrackEntity
import com.bignerdranch.android.playlistmaker.medialibrary.domain.models.PlayListTrackModel
import com.bignerdranch.android.playlistmaker.search.domain.models.TrackModel
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken


class PlaylistTrackDbConverter {

    fun map(playlistTrack: PlayListTrackModel): PlaylistTrackEntity {
        return with(playlistTrack) {
            PlaylistTrackEntity(
                id = id,
                playlistId = Gson().toJson(playlistId),
                track = Gson().toJson(track)
            )
        }
    }

    fun map(playlistTrack: PlaylistTrackEntity): PlayListTrackModel {
        val arrayType = object : TypeToken<MutableList<Int>>() {}.type
        return with(playlistTrack) {
            PlayListTrackModel(
                id = id,
                playlistId = Gson().fromJson(playlistId, arrayType),
                track = Gson().fromJson(track, TrackModel::class.java)

            )
        }
    }
}

