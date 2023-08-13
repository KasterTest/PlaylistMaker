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
    override fun isTrackAlreadyExists(
        playListTrackModels: List<PlayListTrackModel>,
        playlistIdToCheck: Int?
    ): Boolean {
        return playListTrackModels.any { model ->
            playlistIdToCheck != null && model.playlistId.contains(playlistIdToCheck)}
    }

    override suspend fun addTrackToPlaylist(trackModel: PlayListTrackModel) {
        repository.addTrackToPlaylist(trackModel)
    }

    override suspend fun getPlaylistTracks(): List<PlayListTrackModel> {
        return repository.getSavedTrackInPlaylist()
    }

    override suspend fun updateTrakInPlaylist(trackModel: PlayListTrackModel) {
       repository.updateTrackInPlaylist(trackModel)
    }

    override suspend fun updateCountInPlaylist(playlist: PlaylistModel) {
        playlist.tracksCount = playlist.tracksCount + 1
        repository.updatePlaylistTrackCount(playlist)
    }
}
