package com.bignerdranch.android.playlistmaker.medialibrary.domain.db

import com.bignerdranch.android.playlistmaker.medialibrary.domain.models.PlayListTrackModel
import com.bignerdranch.android.playlistmaker.playlist_creator.domain.models.PlaylistModel

interface MessageCreatorRepository {
    fun createMessage(playlist: PlaylistModel, trackModel: List<PlayListTrackModel>): String
}