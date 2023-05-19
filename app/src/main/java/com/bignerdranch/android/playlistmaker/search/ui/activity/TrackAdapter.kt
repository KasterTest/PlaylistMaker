package com.bignerdranch.android.playlistmaker.search.ui.activity

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bignerdranch.android.playlistmaker.search.domain.models.TrackModel

class TrackAdapter(private val trackList: List<TrackModel>) : RecyclerView.Adapter<TrackViewHolder>() {

    var itemClickListener: ((Int, TrackModel) -> Unit)? = null
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrackViewHolder {
        return TrackViewHolder(parent)
    }

    override fun onBindViewHolder(holder: TrackViewHolder, position: Int) {
        val track = trackList[position]
        holder.bind(track)
        holder.itemView.setOnClickListener { itemClickListener?.invoke(position, track)}
    }

    override fun getItemCount(): Int {
        return trackList.size
    }
}