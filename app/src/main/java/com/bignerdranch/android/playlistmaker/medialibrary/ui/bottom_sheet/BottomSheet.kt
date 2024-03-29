package com.practicum.playlistmaker.library.ui.bottom_sheet

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.PorterDuff
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.TextView
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.lifecycle.coroutineScope
import androidx.navigation.fragment.findNavController
import com.bignerdranch.android.playlistmaker.R
import com.bignerdranch.android.playlistmaker.databinding.BottomSheetPlaylistBinding
import com.bignerdranch.android.playlistmaker.medialibrary.ui.models.BottomSheetState
import com.bignerdranch.android.playlistmaker.medialibrary.ui.view_model.BottomSheetViewModel
import com.bignerdranch.android.playlistmaker.player.ui.fragment.PlayerFragment.Companion.KEY_TRACK
import com.bignerdranch.android.playlistmaker.playlist_creator.domain.models.PlaylistModel
import com.bignerdranch.android.playlistmaker.search.domain.models.TrackModel
import com.google.android.material.R.id.design_bottom_sheet
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.gson.Gson

import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class BottomSheet : BottomSheetDialogFragment() {

    private lateinit var binding: BottomSheetPlaylistBinding

    private val viewModel by viewModel<BottomSheetViewModel>()

    private lateinit var playlistsAdapter: BottomSheetAdapter
    private lateinit var track: TrackModel

    override fun onStart() {
        super.onStart()
        setupRatio(requireContext(), dialog as BottomSheetDialog, 100)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = BottomSheetPlaylistBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        track = requireArguments()
            .getString(KEY_TRACK)
            ?.let { Gson().fromJson(it, TrackModel::class.java) } ?: TrackModel.emptyTrack

        initAdapter()
        initListeners()
        initObserver()

    }

    private fun initAdapter() {
        playlistsAdapter = BottomSheetAdapter { playlist ->

            viewModel.onPlaylistClicked(playlist, track)

        }
        binding.playlistsRecycler.adapter = playlistsAdapter
    }

    private fun initListeners() {
        binding.createPlaylistBtn.setOnClickListener {
            findNavController().navigate(
                R.id.action_bottomSheet_to_newPlaylistFragment
            )
        }
    }

    private fun initObserver() {
        viewLifecycleOwner.lifecycle.coroutineScope.launch {
            viewModel.contentFlow.collect { screenState ->
                render(screenState)
            }
        }
    }

    private fun render(state: BottomSheetState) {
        when (state) {
            is BottomSheetState.AddedAlready -> {
                val message =
                    getString(R.string.already_added) + " \"" + state.playlistModel.playlistName + "\" "
                Toast(requireContext()).showMessage(message)
            }

            is BottomSheetState.AddedNow -> {
                val message =
                    getString(R.string.added) + " \"" + state.playlistModel.playlistName + "\" "

                Toast(requireContext()).showMessage(message).apply {  }
                dialog?.cancel()
            }

            else -> showContent(state.content)
        }
    }

    fun Toast.showMessage(message: String)
    {
        val layout = requireActivity().layoutInflater.inflate (
            R.layout.custom_already_added_toast_layout,
            requireActivity().findViewById(R.id.toast_container)
        )
        val textView = layout.findViewById<TextView>(R.id.toast_text)
        textView.text = message

        this.apply {
            duration = Toast.LENGTH_SHORT
            setGravity(Gravity.TOP or Gravity.FILL_HORIZONTAL, 0, resources.getDimensionPixelOffset(R.dimen.toast_margin_top))
            view = layout
            show()
        }
    }


    private fun showContent(content: List<PlaylistModel>) {
        binding.playlistsRecycler.visibility = View.VISIBLE
        playlistsAdapter.apply {
            list.clear()
            list.addAll(content)
            notifyDataSetChanged()
        }
    }

    private fun setupRatio(context: Context, bottomSheetDialog: BottomSheetDialog, percetage: Int) {

        val bottomSheet = bottomSheetDialog.findViewById<View>(design_bottom_sheet) as FrameLayout
        bottomSheet.backgroundTintMode = PorterDuff.Mode.CLEAR
        bottomSheet.backgroundTintList = ColorStateList.valueOf(Color.TRANSPARENT)
        bottomSheet.setBackgroundColor(Color.TRANSPARENT)
        val behavior: BottomSheetBehavior<*> = BottomSheetBehavior.from(bottomSheet)
        val layoutParams = bottomSheet.layoutParams
        layoutParams.height = getBottomSheetDialogDefaultHeight(context, percetage)
        bottomSheet.layoutParams = layoutParams
        behavior.state = BottomSheetBehavior.STATE_COLLAPSED

    }

    private fun getBottomSheetDialogDefaultHeight(context: Context, percetage: Int): Int {
        return getWindowHeight(context) * percetage / 100
    }

    private fun getWindowHeight(context: Context): Int {
        val displayMetrics = DisplayMetrics()

        @Suppress("DEPRECATION") requireActivity().windowManager.defaultDisplay.getMetrics(
            displayMetrics
        )

        return displayMetrics.heightPixels
    }

    companion object {

        fun createArgs(track: TrackModel): Bundle {
            val trackJson = Gson().toJson(track)
            return bundleOf(KEY_TRACK to trackJson)
        }

        private const val MESSAGE_DURATION = 2000
    }
}