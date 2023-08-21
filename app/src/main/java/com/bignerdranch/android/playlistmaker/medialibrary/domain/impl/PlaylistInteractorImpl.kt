package com.bignerdranch.android.playlistmaker.medialibrary.domain.impl


import com.bignerdranch.android.playlistmaker.medialibrary.domain.db.PlaylistsInteractor
import com.bignerdranch.android.playlistmaker.medialibrary.domain.db.PlaylistsRepository
import com.bignerdranch.android.playlistmaker.medialibrary.domain.models.PlayListTrackModel
import com.bignerdranch.android.playlistmaker.playlist_creator.domain.models.PlaylistModel
import kotlinx.coroutines.flow.Flow

class PlaylistInteractorImpl(private val repository: PlaylistsRepository) : PlaylistsInteractor {
    
    override suspend fun getPlaylists(): Flow<List<PlaylistModel>> {
        return repository.getSavedPlaylists()
    }


    override suspend fun addTrackToPlaylist(playlist: Int, trackPlaylist: PlayListTrackModel) {
        repository.addTrackToPlaylist(playlist, trackPlaylist)
    }
    override suspend fun getPlaylistTracks(): List<PlayListTrackModel> {
        return repository.getSavedTrackInPlaylist()
    }

    override suspend fun deleteTrack(playlist: PlaylistModel, trackModel: PlayListTrackModel) {
        repository.deleteTrackFromPlaylist(playlist, trackModel)
    }

    override suspend fun deletePlaylist(playlist: PlaylistModel) {
        repository.deletePlaylist(playlist)
    }

    override suspend fun getPlaylist(id: Int): Flow<PlaylistModel> {
        return repository.getSavedPlaylist(id)
    }

    override suspend fun getTracksFromPlaylist(playlist: PlaylistModel): List<PlayListTrackModel> {
        return repository.getTracksInPlaylist(playlist.id)
    }

    override suspend fun isPlaylistEmpty(playlist: PlaylistModel, trackPlaylist: PlayListTrackModel): Boolean {
        return repository.isPlaylistEmpty(playlist, trackPlaylist)
    }

}
