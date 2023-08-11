package com.bignerdranch.android.playlistmaker.medialibrary.domain.impl


import com.bignerdranch.android.playlistmaker.medialibrary.domain.db.PlaylistsInteractor
import com.bignerdranch.android.playlistmaker.medialibrary.domain.db.PlaylistsRepository
import com.bignerdranch.android.playlistmaker.playlist_creator.domain.models.PlaylistModel
import com.bignerdranch.android.playlistmaker.search.domain.models.TrackModel
import kotlinx.coroutines.flow.Flow

class PlaylistInteractorImpl(private val repository: PlaylistsRepository) : PlaylistsInteractor {
    
    override fun getPlaylists(): Flow<List<PlaylistModel>> {
        return repository.getSavedPlaylists()
    }
    
    override fun isTrackAlreadyExists(playlist: PlaylistModel, track: TrackModel) =
        playlist.trackList.contains(track)
    
    override suspend fun addTrackToPlaylist(playlist: PlaylistModel, track: TrackModel) {
        playlist.trackList = playlist.trackList + track
        playlist.tracksCount = playlist.trackList.size
        repository.updateTracks(playlist)
    }
}