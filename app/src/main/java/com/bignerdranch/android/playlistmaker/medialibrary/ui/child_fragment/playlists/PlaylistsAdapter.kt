package com.bignerdranch.android.playlistmaker.medialibrary.ui.child_fragment.playlists

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bignerdranch.android.playlistmaker.databinding.ViewPlaylistItemBinding
import com.bignerdranch.android.playlistmaker.playlist_creator.domain.models.PlaylistModel


class PlaylistsAdapter(private val clickListener: PlaylistClickListener) :
    RecyclerView.Adapter<PlaylistsViewHolder>() {
    
    val playlists = ArrayList<PlaylistModel>()
    
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlaylistsViewHolder {
       return PlaylistsViewHolder(
            ViewPlaylistItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }
    
    override fun getItemCount() = playlists.size
    
    override fun onBindViewHolder(holder: PlaylistsViewHolder, position: Int) {
        val playlistItem = playlists[holder.adapterPosition]
        holder.bind(playlistItem)
        holder.itemView.setOnClickListener { clickListener.onTrackClick(playlistItem) }
    }
    
    fun interface PlaylistClickListener {
        fun onTrackClick(playlist: PlaylistModel)
    }
}