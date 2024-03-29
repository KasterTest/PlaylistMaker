package com.bignerdranch.android.playlistmaker.search.ui.fragment

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bignerdranch.android.playlistmaker.R
import com.bignerdranch.android.playlistmaker.search.domain.models.TrackModel
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import java.text.SimpleDateFormat
import java.util.*


class TrackViewHolder(parent: ViewGroup) : RecyclerView.ViewHolder(
    LayoutInflater.from(parent.context).inflate(R.layout.track_item, parent, false)
) {
    private val textViewTrackName: TextView = itemView.findViewById(R.id.text_view_track_name)
    private val textViewArtistName: TextView = itemView.findViewById(R.id.text_view_artist_name)
    private val textViewTrackTime: TextView = itemView.findViewById(R.id.text_view_track_time)
    private val imageViewArtwork: ImageView = itemView.findViewById(R.id.image_view_artwork)

    fun bind(track: TrackModel) {
        textViewTrackName.text = track.trackName
        textViewArtistName.text = track.artistName
        textViewTrackTime.text = SimpleDateFormat("mm:ss", Locale.getDefault()).format(track.trackTimeMillis).toString()
        Glide.with(itemView)
            .load(track.artworkUrl100)
            .placeholder(R.drawable.no_replay)
            .transform(RoundedCorners(8))
            .into(imageViewArtwork)
        itemView.setOnClickListener {
        }
    }
}