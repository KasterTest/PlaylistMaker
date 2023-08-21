package com.bignerdranch.android.playlistmaker.playlist_redactor.fragment

import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.navigation.fragment.findNavController
import com.bignerdranch.android.playlistmaker.R
import com.bignerdranch.android.playlistmaker.databinding.FragmentNewPlaylistBinding
import com.bignerdranch.android.playlistmaker.playlist_creator.domain.models.PlaylistModel
import com.bignerdranch.android.playlistmaker.playlist_creator.ui.fragment.NewPlaylistFragment
import com.bignerdranch.android.playlistmaker.playlist_menu.ui.bottom_sheet.BottomSheetMenu
import com.bignerdranch.android.playlistmaker.playlist_redactor.view_model.PlaylistRedactorViewModel
import com.bignerdranch.android.playlistmaker.utils.viewBinding
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.google.android.material.snackbar.Snackbar
import com.google.gson.Gson
import org.koin.androidx.viewmodel.ext.android.viewModel

class PlaylistRedactorFragment : NewPlaylistFragment() {

    override val viewModel by viewModel<PlaylistRedactorViewModel>()
    private val binding by viewBinding<FragmentNewPlaylistBinding>()
    private var playlist: PlaylistModel? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        playlist = requireArguments()
            .getString(BottomSheetMenu.PLAYLIST_KEY)
            ?.let { Gson().fromJson(it, PlaylistModel::class.java) } ?: PlaylistModel.emptyPlaylist

        playlist?.let {
            drawPlaylist(it)
            viewModel.initPlaylist(it)
        }
    }

    override fun showMessage(playlistName: String) {
        val parentView = requireActivity().findViewById<View>(android.R.id.content)
        val message =
            getString(R.string.playlist) + " \"" + playlistName + "\" " + getString(R.string.changed)
        Snackbar
            .make(
                parentView,
                message,
                Snackbar.LENGTH_SHORT
            )
            .setBackgroundTint(ContextCompat.getColor(requireContext(), R.color.bottom_snackbar_background))
            .setTextColor(ContextCompat.getColor(requireContext(), R.color.bottom_snackbar_text))
            .setDuration(MESSAGE_DURATION)
            .show()
    }



    override fun initBackPressed() {
        binding.navigationToolbar.setNavigationOnClickListener {
            findNavController().navigateUp()
        }
    }

    private fun drawPlaylist(playlist: PlaylistModel) {

        val cornerRadius =
            requireContext().resources.getDimensionPixelSize(R.dimen.corner_radius_8dp)

        with(binding) {
            toolbarTitle.text = getString(R.string.edit_title)
            Glide
                .with(playlistCoverImage.context)
                .load(playlist.coverImageUrl)
                .placeholder(R.drawable.no_replay)
                .transform(CenterCrop(), RoundedCorners(cornerRadius))
                .into(playlistCoverImage)
            playlistName.setText(playlist.playlistName)
            playlistDescription.setText(playlist.playlistDescription)

            buttonCreate.text = getString(R.string.save_playlist)
        }

    }

    companion object {

        private const val PLAYLIST_KEY = "playlist_key"
        private const val MESSAGE_DURATION_MILLIS = 2000

        fun createArgs(playlist: PlaylistModel): Bundle = bundleOf(
            PLAYLIST_KEY to Gson().toJson(playlist)
        )
    }
}