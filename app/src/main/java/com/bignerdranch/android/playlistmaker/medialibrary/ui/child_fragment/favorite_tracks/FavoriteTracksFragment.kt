package com.bignerdranch.android.playlistmaker.medialibrary.ui.child_fragment.favorite_tracks
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.bignerdranch.android.playlistmaker.R
import com.bignerdranch.android.playlistmaker.core.root.RootActivity
import com.bignerdranch.android.playlistmaker.databinding.FavoriteTracksFragmentBinding
import com.bignerdranch.android.playlistmaker.medialibrary.domain.models.FavoriteState
import com.bignerdranch.android.playlistmaker.medialibrary.ui.view_model.FavoriteTracksViewModel
import com.bignerdranch.android.playlistmaker.player.ui.fragment.PlayerFragment
import com.bignerdranch.android.playlistmaker.search.domain.models.TrackModel
import com.bignerdranch.android.playlistmaker.search.ui.fragment.TrackAdapter
import com.bignerdranch.android.playlistmaker.utils.debounce
import org.koin.androidx.viewmodel.ext.android.viewModel

class FavoriteTracksFragment : Fragment() {

    private val viewModel by viewModel<FavoriteTracksViewModel>()
    private lateinit var trackAdapter: TrackAdapter
    private lateinit var binding: FavoriteTracksFragmentBinding
    private lateinit var onTrackClickDebounce: (TrackModel) -> Unit
    private val trackList = ArrayList<TrackModel>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FavoriteTracksFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupViews()
        observeOnContentState()
        viewModel.fillData()
        onTrackClickDebounce = debounce<TrackModel>(CLICK_DEBOUNCE_DELAY, viewLifecycleOwner.lifecycleScope, false) { track ->
            navigateToPlayer(track)
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.fillData()
    }

    private fun setupViews() {
        trackAdapter = TrackAdapter(trackList)
        binding.tracksList.adapter = trackAdapter

        trackAdapter.itemClickListener = { position, track ->
            (activity as? RootActivity)?.animateBottomNavigationView()
            onTrackClickDebounce(track)
        }
    }

    private fun observeOnContentState() {
        viewModel.observeContentState().observe(viewLifecycleOwner) { contentState ->
            render(contentState)
        }
    }

    private fun render(state: FavoriteState) {
        when (state) {
            is FavoriteState.FavoriteTracks -> showContent(state.trackList)
            FavoriteState.Empty -> showMessage()
        }
    }

    private fun showContent(list: List<TrackModel>) {
        binding.placeholder.isVisible = false
        binding.tracksList.isVisible = true
        trackList.clear()
        trackList.addAll(list)
        trackAdapter.notifyDataSetChanged()
    }

    private fun showMessage() {
        binding.placeholder.isVisible = true
        binding.tracksList.isVisible = false
    }

    private fun navigateToPlayer(track: TrackModel) {
            track.isFavorite = true
            findNavController().navigate(R.id.action_mediaLibraryFragment_to_playerFragment,
                PlayerFragment.createArgs(track))
    }

    companion object {
        fun newInstance() = FavoriteTracksFragment()
        private const val CLICK_DEBOUNCE_DELAY = 200L
    }
}