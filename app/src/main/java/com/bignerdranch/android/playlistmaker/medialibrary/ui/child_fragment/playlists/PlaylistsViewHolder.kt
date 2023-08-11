package com.bignerdranch.android.playlistmaker.medialibrary.ui.child_fragment.playlists

import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bignerdranch.android.playlistmaker.R
import com.bignerdranch.android.playlistmaker.databinding.ViewPlaylistItemBinding
import com.bignerdranch.android.playlistmaker.playlist_creator.domain.models.PlaylistModel
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners


class PlaylistsViewHolder(
    private val binding: ViewPlaylistItemBinding,
) : RecyclerView.ViewHolder(binding.root) {
    
    fun bind(model: PlaylistModel) {
        val cornerRadius = itemView.resources.getDimensionPixelSize(R.dimen.corner_radius_8dp)
        
        binding.playlistName.text = model.playlistName
        binding.tracksCount.text = itemView.resources.getQuantityString(R.plurals.tracks, model.tracksCount, model.tracksCount)
        
        binding.playlistCover.setImage(
            url = model.coverImageUrl,
            placeholder = R.drawable.no_replay,
            cornerRadius = cornerRadius,
        )
    }

    fun ImageView.setImage(url: String, placeholder: Int, cornerRadius: Int) {
        Glide
            .with(this.context)
            .load(url)
            .placeholder(placeholder)
            .transform(CenterCrop(), RoundedCorners(cornerRadius))
            .into(this)
    }
}
