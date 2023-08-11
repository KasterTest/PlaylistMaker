package com.bignerdranch.android.playlistmaker.medialibrary.data.converters

import com.bignerdranch.android.playlistmaker.medialibrary.data.db.entity.PlaylistEntity
import com.bignerdranch.android.playlistmaker.playlist_creator.domain.models.PlaylistModel
import com.bignerdranch.android.playlistmaker.search.domain.models.TrackModel
import com.google.gson.Gson
import java.util.Date

class PlaylistDbConverter {

    fun map(playlist: PlaylistModel): PlaylistEntity {
        return with(playlist) {
            PlaylistEntity(
                id = id,
                playlistName = playlistName,
                playlistDescription = playlistDescription,
                imageUrl = coverImageUrl,
                trackList = Gson().toJson(trackList),
                countTracks = tracksCount,
                saveDate = Date(),
            )
        }
    }

    fun map(playlist: PlaylistEntity): PlaylistModel {
        return with(playlist) {
            PlaylistModel(
                id = id,
                playlistName = playlistName,
                playlistDescription = playlistDescription,
                coverImageUrl = imageUrl,
                trackList = Gson().fromJson(trackList, Array<TrackModel>::class.java).toMutableList(),
                tracksCount = countTracks,
            )
        }
    }
}