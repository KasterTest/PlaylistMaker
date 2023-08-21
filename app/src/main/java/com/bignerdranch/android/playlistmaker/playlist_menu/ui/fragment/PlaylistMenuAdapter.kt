package com.bignerdranch.android.playlistmaker.playlist_menu.ui.fragment

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bignerdranch.android.playlistmaker.search.domain.models.TrackModel

class PlaylistMenuAdapter(private val trackList: List<TrackModel>) : RecyclerView.Adapter<PlaylistMenuViewHolder>() {

    var itemClickListener: ((Int, TrackModel) -> Unit)? = null
    var itemLongClickListener: ((Int, TrackModel) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlaylistMenuViewHolder {
        return PlaylistMenuViewHolder(parent)
    }

    override fun onBindViewHolder(holder: PlaylistMenuViewHolder, position: Int) {
        val track = trackList[position]
        holder.bind(track)
        holder.itemView.setOnClickListener { itemClickListener?.invoke(position, track) }
        holder.itemView.setOnLongClickListener {
            itemLongClickListener?.invoke(position, track)
            true
        }
    }

    override fun getItemCount(): Int {
        return trackList.size
    }
}
