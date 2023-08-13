package com.bignerdranch.android.playlistmaker.medialibrary.data.converters

import com.bignerdranch.android.playlistmaker.medialibrary.data.db.entity.PlaylistEntity
import com.bignerdranch.android.playlistmaker.playlist_creator.domain.models.PlaylistModel
import java.util.Date

class PlaylistDbConverter {

    fun map(playlist: PlaylistModel): PlaylistEntity {
        return with(playlist) {
            PlaylistEntity(
                id = id,
                playlistName = playlistName,
                playlistDescription = playlistDescription,
                imageUrl = coverImageUrl,
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
                tracksCount = countTracks,
            )
        }
    }
}