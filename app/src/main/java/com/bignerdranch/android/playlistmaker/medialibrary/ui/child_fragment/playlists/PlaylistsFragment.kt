package com.bignerdranch.android.playlistmaker.medialibrary.ui.child_fragment.playlists
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.bignerdranch.android.playlistmaker.R
import com.bignerdranch.android.playlistmaker.databinding.FragmentPlaylistsBinding
import com.bignerdranch.android.playlistmaker.medialibrary.ui.models.PlaylistsScreenState
import com.bignerdranch.android.playlistmaker.medialibrary.ui.view_model.PlaylistsViewModel
import com.bignerdranch.android.playlistmaker.playlist_creator.domain.models.PlaylistModel
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class PlaylistsFragment : Fragment() {

    private val viewModel by viewModel<PlaylistsViewModel>()

    private lateinit var playlistsAdapter: PlaylistsAdapter
    private lateinit var binding: FragmentPlaylistsBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentPlaylistsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initAdapter()

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.contentFlow.collect { screenState ->
                render(screenState)
            }
        }

        binding.newPlaylistBtn.setOnClickListener {
            findNavController().navigate(
                R.id.action_mediaLibraryFragment_to_newPlaylistFragment
            )
        }
    }

    private fun render(state: PlaylistsScreenState) {
        when (state) {
            is PlaylistsScreenState.Content -> showContent(state.playlists)
            PlaylistsScreenState.Empty -> showPlaceholder()
        }
    }

    private fun showPlaceholder() {
        binding.placeholdersGroup.isVisible = true
        binding.playlists.isVisible = false
    }

    private fun showContent(content: List<PlaylistModel>) {
        binding.placeholdersGroup.isVisible = false
        binding.playlists.isVisible = true

        playlistsAdapter.apply {
            playlists.clear()
            playlists.addAll(content)
            notifyDataSetChanged()
        }
    }

    private fun initAdapter() {
        playlistsAdapter = PlaylistsAdapter { playlist ->
            Snackbar.make(requireView(), "Clicked", Snackbar.LENGTH_SHORT).show()
        }

        binding.playlists.apply {
            adapter = playlistsAdapter
            addItemDecoration(PlaylistsOffsetItemDecoration(requireContext()))
        }
    }

    companion object {
        fun newInstance() = PlaylistsFragment()
    }
}