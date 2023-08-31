package com.bignerdranch.android.playlistmaker.playlist_menu.ui.bottom_sheet

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.bignerdranch.android.playlistmaker.R
import com.bignerdranch.android.playlistmaker.databinding.BottomSheetMenuBinding
import com.bignerdranch.android.playlistmaker.playlist_creator.domain.models.PlaylistModel
import com.bignerdranch.android.playlistmaker.playlist_menu.ui.view_model.PlaylistMenuViewModel
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.gson.Gson
import org.koin.androidx.viewmodel.ext.android.viewModel
import com.bignerdranch.android.playlistmaker.playlist_redactor.fragment.PlaylistRedactorFragment
import kotlinx.coroutines.launch

class BottomSheetMenu : BottomSheetDialogFragment() {

    private lateinit var binding: BottomSheetMenuBinding
    private val viewModel by viewModel<PlaylistMenuViewModel>()
    private var playlist: PlaylistModel? = null

    override fun onStart() {
        super.onStart()
        setupBottomSheet()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = BottomSheetMenuBinding.inflate(inflater, container, false)
        binding.root.setBackgroundResource(R.drawable.bottom_sheet)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupUI()
    }

    private fun setupBottomSheet() {
        val bottomSheetSetuper = BottomSheetSetuper(requireActivity())
        bottomSheetSetuper.setupRatio(dialog as BottomSheetDialog, PERCENT_OCCUPIED_BY_BOTTOM_SHEET)
    }

    private fun setupUI() {
        playlist = requireArguments().getString(PLAYLIST_KEY)
            ?.let { Gson().fromJson(it, PlaylistModel::class.java) } ?: PlaylistModel.emptyPlaylist

        playlist?.let { drawPlaylistItem(it) }
        setupClickListeners()
    }

    private fun drawPlaylistItem(playlist: PlaylistModel) {
        with(binding.bottomSheetMenuItem) {
            Glide.with(ivCover.context)
                .load(playlist.coverImageUrl)
                .placeholder(R.drawable.no_replay)
                .transform(CenterCrop())
                .into(ivCover)

            tvPlaylistName.text = playlist.playlistName
            tvTracksCount.text = resources.getQuantityString(
                R.plurals.tracks, playlist.tracksCount, playlist.tracksCount
            )

        }

    }

    private fun setupClickListeners() {
        with(binding) {
            tvShare.setOnClickListener { sharePlaylist() }
            tvEdit.setOnClickListener { openEditor() }
            tvDelete.setOnClickListener { showDialog() }
        }
    }

    private fun openEditor() {
        findNavController().navigate(
            R.id.action_bottomSheetMenu_to_playlistRedactorFragment,
            playlist?.let { PlaylistRedactorFragment.createArgs(it) }
        )
    }

    private fun sharePlaylist() {
        lifecycleScope.launch {
            if (viewModel.isTracksEmpty(playlist!!)) {
                showMessage(getString(R.string.empty_track_list))
            } else {
                viewModel.messagesCreator(requireContext(), playlist!!)
                }
            }
        }


    private fun showMessage(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }

    private fun showDialog() {
        val dialog = MaterialAlertDialogBuilder(requireContext())
            .setTitle(getString(R.string.delete_playlist) + " ${playlist?.playlistName}?")
            .setNegativeButton(getString(R.string.no)) { _, _ -> }
            .setPositiveButton(getString(R.string.yes)) { _, _ ->
                playlist?.let { viewModel.deletePlaylist(it) }
                goBack()
            }
            .show()

        val positiveButton = dialog.getButton(AlertDialog.BUTTON_POSITIVE)
        val neutralButton = dialog.getButton(AlertDialog.BUTTON_NEGATIVE)
        positiveButton.setTextColor(ContextCompat.getColor(requireContext(), R.color.blue))
        neutralButton.setTextColor(ContextCompat.getColor(requireContext(), R.color.blue))
        val window = dialog.window
        window?.setBackgroundDrawableResource(R.drawable.aller_custom_back)
        val title = dialog.findViewById<TextView>(com.google.android.material.R.id.alertTitle)
        title?.setTextColor(ContextCompat.getColor(requireContext(), R.color.allert_title_color))
    }

    private fun goBack() {
        findNavController().popBackStack(R.id.mediaLibraryFragment, false)
    }

    companion object {
        const val PLAYLIST_KEY = "playlist_key"
        private const val PERCENT_OCCUPIED_BY_BOTTOM_SHEET = 0.45f

        fun createArgs(playlist: PlaylistModel): Bundle = bundleOf(
            PLAYLIST_KEY to Gson().toJson(playlist)
        )
    }
}