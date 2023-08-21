package com.bignerdranch.android.playlistmaker.playlist_menu.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.coroutineScope
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.bignerdranch.android.playlistmaker.R
import com.bignerdranch.android.playlistmaker.databinding.FragmentPlaylistMenuBinding
import com.bignerdranch.android.playlistmaker.player.ui.fragment.PlayerFragment
import com.bignerdranch.android.playlistmaker.playlist_creator.domain.models.PlaylistModel
import com.bignerdranch.android.playlistmaker.playlist_menu.ui.bottom_sheet.BottomSheetMenu
import com.bignerdranch.android.playlistmaker.playlist_menu.ui.bottom_sheet.BottomSheetSetuper
import com.bignerdranch.android.playlistmaker.playlist_menu.ui.models.PlaylistMenuScreenState
import com.bignerdranch.android.playlistmaker.playlist_menu.ui.models.PlaylistMenuState
import com.bignerdranch.android.playlistmaker.playlist_menu.ui.view_model.PlaylistMenuViewModel
import com.bignerdranch.android.playlistmaker.search.domain.models.TrackModel
import com.bignerdranch.android.playlistmaker.utils.debounce
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class PlaylistMenuFragment : Fragment() {

    private lateinit var binding: FragmentPlaylistMenuBinding
    private var onTrackClickDebounce: ((TrackModel) -> Unit)? = null
    private var onLongTrackClickDebounce: ((TrackModel) -> Unit)? = null
    private val viewModel by viewModel<PlaylistMenuViewModel>()
    private val trackList = ArrayList<TrackModel>()
    private var trackAdapter = PlaylistMenuAdapter(trackList)

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentPlaylistMenuBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initPlaylist()
        initObserver()
        initBottomSheetBehavior()
        initAdapter()
        initListeners()

    }

    override fun onDestroyView() {
        super.onDestroyView()
            //trackAdapter = emptyParametersHolder()
    }

    private fun initPlaylist() {
        val playlistId: Int = requireArguments().getInt(KEY_ID)

        viewModel.fillData(playlistId = playlistId)
    }

    private fun refreshPlaylistInfo() {

        val playlistDuration: Int = viewModel.getPlaylistDuration()
        val tracksCount: Int = viewModel.getTracksCount()

        binding.tvPlaylistDuration.text =
            resources.getQuantityString(R.plurals.minutes, playlistDuration, playlistDuration)

        binding.tvPlaylistTracksCount.text = resources.getQuantityString(
            R.plurals.tracks, tracksCount, tracksCount
        )
    }

    private fun drawPlaylist(playlistModel: PlaylistModel) {
        with(binding) {
            Glide.with(ivPlaylistCover.context)
                .load(playlistModel.coverImageUrl)
                .placeholder(R.drawable.no_replay)
                .transform(CenterCrop())
                .into(ivPlaylistCover)

            tvPlaylistName.text = playlistModel.playlistName
            tvPlaylistDescription.text = playlistModel.playlistDescription

            if (playlistModel.playlistDescription.isEmpty()) {
                tvPlaylistDescription.visibility = View.GONE
            }
        }
    }

    private fun initBottomSheetBehavior() {
        val bottomSheetSetuper = BottomSheetSetuper(activity)
        bottomSheetSetuper.setupRatio(
            container = binding.bottomSheetTrackList, percentage = PERCENT_OCCUPIED_BY_BOTTOM_SHEET
        )
    }

    private fun initAdapter() {
        binding.rvTrackList.apply {
            adapter = trackAdapter
            layoutManager = LinearLayoutManager(context)
        }
    }

    private fun initListeners() {
        onTrackClickDebounce = debounce<TrackModel>(CLICK_DEBOUNCE_DELAY_MILLIS,
            coroutineScope = viewLifecycleOwner.lifecycleScope,
            useLastParam = false)  { track ->
            findNavController().navigate(
            R.id.action_playlistMenuFragment_to_playerFragment,
            PlayerFragment.createArgs(track))
        }

        onLongTrackClickDebounce = debounce<TrackModel>(CLICK_DEBOUNCE_DELAY_MILLIS,
            coroutineScope = viewLifecycleOwner.lifecycleScope,
            useLastParam = false)  { track ->
            showDialog(track)
        }

        trackAdapter.itemClickListener = { position, track ->
            onTrackClickDebounce?.let { it(track) }
        }

        trackAdapter.itemLongClickListener = { position, track ->
            onLongTrackClickDebounce?.let { it(track) }
        }

        with(binding) {

            tbNavigation.setNavigationOnClickListener {
                goBack()
            }

            ivShare.setOnClickListener {
                viewModel.shareClicked()
            }

            ivMore.setOnClickListener {
                openMenu(viewModel.getPlaylist())
            }
        }
    }

    private fun openMenu(playlist: PlaylistModel) {
        findNavController().navigate(
            R.id.action_playlistMenuFragment_to_bottomSheetMenu,
            BottomSheetMenu.createArgs(playlist)
        )
    }

    private fun goBack() {
        findNavController().navigateUp()
    }

    private fun initObserver() {
        viewLifecycleOwner.lifecycle.coroutineScope.launch {
            viewModel.contentFlow.collect { screenState ->
                render(screenState)
            }
        }
    }

    private fun render(state: PlaylistMenuState) {
        when (state) {
            is PlaylistMenuState.Content -> showContent(state.content, state.bottomListState)
            PlaylistMenuState.EmptyShare -> showMessage()
            is PlaylistMenuState.Share -> {
                val messageCreator = MessageCreator(requireContext(), trackList)
                viewModel.sharePlaylist(messageCreator.create(state.playlist))
            }
            PlaylistMenuState.DefaultState -> {}
        }
    }

    private fun showContent(content: PlaylistModel, bottomListState: PlaylistMenuScreenState) {
        drawPlaylist(content)
        refreshPlaylistInfo()
        when (bottomListState) {
            is PlaylistMenuScreenState.Content<*> -> {
                with(binding) {
                    rvTrackList.visibility = View.VISIBLE
                    ivPlaceholder.visibility = View.GONE
                    tvPlaceholder.visibility = View.GONE
                }
                trackAdapter?.apply {
                    trackList.clear()
                    trackList.addAll(bottomListState.contentList as List<TrackModel>)
                    notifyDataSetChanged()
                }

            }
            PlaylistMenuScreenState.Empty -> showPlaceholder()
            else -> {}
        }
    }

    private fun showPlaceholder() {
        with(binding) {
            rvTrackList.visibility = View.GONE
            ivPlaceholder.visibility = View.VISIBLE
            tvPlaceholder.visibility = View.VISIBLE
        }
    }

    private fun showMessage() {
        val message = getString(R.string.empty_track_list)
        Snackbar
            .make(requireContext(), binding.containerLayout, message, Snackbar.LENGTH_SHORT)
            .setBackgroundTint(ContextCompat.getColor(requireContext(), R.color.blue))
            .setTextColor(ContextCompat.getColor(requireContext(), R.color.white))
            .setDuration(MESSAGE_DURATION_MILLIS)
            .show()
    }

    private fun showDialog(track: TrackModel) {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle(getString(R.string.delete_track))
            .setNegativeButton(getString(R.string.no)) { _, _ -> }
            .setPositiveButton(getString(R.string.yes)) { _, _ ->

                viewModel.deleteTrack(track)
                refreshPlaylistInfo()
            }
            .show()
    }

    companion object {

        const val KEY_ID = "key_id"
        private const val PERCENT_OCCUPIED_BY_BOTTOM_SHEET = 0.30f
        private const val CLICK_DEBOUNCE_DELAY_MILLIS = 300L
        private const val MESSAGE_DURATION_MILLIS = 2000

        fun createArgs(id: Int): Bundle = bundleOf(
            KEY_ID to id
        )
    }
}