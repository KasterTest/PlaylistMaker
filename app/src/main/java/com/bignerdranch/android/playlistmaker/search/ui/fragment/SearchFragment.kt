package com.bignerdranch.android.playlistmaker.search.ui.fragment

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.bignerdranch.android.playlistmaker.*
import com.bignerdranch.android.playlistmaker.search.data.storage.TrackSearchState
import com.bignerdranch.android.playlistmaker.search.domain.models.TrackModel
import com.bignerdranch.android.playlistmaker.search.ui.models.SearchUIState
import com.bignerdranch.android.playlistmaker.search.ui.view_model.SearchViewModel
import com.bignerdranch.android.playlistmaker.databinding.FragmentSearchBinding
import com.google.gson.Gson
import org.koin.androidx.viewmodel.ext.android.viewModel


class SearchFragment : Fragment() {

    companion object {
        const val SEARCH_STRING = "SEARCH_STRING"
        private const val SEARCH_DEBOUNCE_DELAY = 2000L
        private const val CLICK_DEBOUNCE_DELAY = 1000L
    }


    private lateinit var binding: FragmentSearchBinding

    private val viewModel by viewModel<SearchViewModel>()
    private val handler = Handler(Looper.getMainLooper())
    private val searchRunnable = Runnable { viewModel.searchTracks(binding.inputEditText.text.toString()) }
    private var isClickAllowed = true
    private val trackList = ArrayList<TrackModel>()
    private val trackAdapter = TrackAdapter(trackList)

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(SEARCH_STRING, binding.inputEditText.text.toString())
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentSearchBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        configureUI()
        setupListeners()
        observeViewModel()
    }

    override fun onDestroy() {
        super.onDestroy()
    }

    private fun configureUI() {
        setupRecyclerView()
        setupTextWatcher()
    }

    private fun setupRecyclerView() {
        binding.recyclerView.apply {
            adapter = trackAdapter
            layoutManager = LinearLayoutManager(context)
        }
    }

    private fun observeViewModel() {
        viewModel.trackSearchState.observe(viewLifecycleOwner) { state ->
            when (state) {
                is TrackSearchState.Error -> updateUI(SearchUIState.CONNECTION_ERROR)
                TrackSearchState.Loading -> updateUI(SearchUIState.LOADING)
                TrackSearchState.NotFound -> updateUI(SearchUIState.EMPTY_SEARCH)
                is TrackSearchState.StateVisibleHistory -> showHistoryList(binding.inputEditText.text.toString(), state.tracks)
                is TrackSearchState.Success -> showSearchList(state.tracks)
            }
        }
    }

    private fun setupListeners() {
        binding.clearIcon.setOnClickListener {
            clearEditText()
            viewModel.getSearchHistoryList()
        }
        binding.clearHistory.setOnClickListener {
            handler.removeCallbacks(searchRunnable)
            viewModel.clearSearchHistory()
            viewModel.getSearchHistoryList()
        }

        binding.updateButton.setOnClickListener {
            viewModel.searchTracks(binding.inputEditText.text.toString())
        }


        trackAdapter.itemClickListener = { position, track ->
            if (isClickAllowed) {
                isClickAllowed = false
                handler.postDelayed({ isClickAllowed = true }, CLICK_DEBOUNCE_DELAY)
                viewModel.addTrackToSearchHistory(track)
                navController(track)
            }

        }
    }
    private fun navController(track: TrackModel) {
        val bundle = Bundle().apply {
            putString("TRACK_INFO", Gson().toJson(track))
        }

        findNavController().navigate(
            R.id.action_searchFragment_to_playerActivity,
            bundle
        )
    }




    private fun showHistoryList (inputText: CharSequence?, tracks: List<TrackModel>) {
        binding.apply {
            progressBar.visibility = View.GONE
            placeholderMessage.visibility = View.GONE
            nothingSearchImage.visibility = View.GONE
            updateButton.visibility = View.GONE
        }
        if (inputText?.isEmpty() == true && tracks.isNotEmpty()) {
            binding.labelSearch.visibility = View.VISIBLE
            binding.clearHistory.visibility = View.VISIBLE
            trackList.clear()
            trackList.addAll(tracks)
            trackAdapter.notifyDataSetChanged()
        } else {
            binding.labelSearch.visibility = View.GONE
            binding.clearHistory.visibility = View.GONE
            trackList.clear()
            trackAdapter.notifyDataSetChanged()
        }
    }

    private fun showSearchList (tracks: List<TrackModel>) {
        binding.apply {
            placeholderMessage.visibility = View.GONE
            nothingSearchImage.visibility = View.GONE
            updateButton.visibility = View.GONE
            progressBar.visibility = View.GONE
            labelSearch.visibility = View.GONE
            clearHistory.visibility = View.GONE
        }
        trackAdapter.apply {
            trackList.clear()
            trackList.addAll(tracks)
            notifyDataSetChanged()
        }
    }

    private fun clearEditText () {
        handler.removeCallbacks(searchRunnable)
        binding.inputEditText.setText("")
        binding.inputEditText.onEditorAction(EditorInfo.IME_ACTION_DONE)
        trackList.clear()
        trackAdapter.notifyDataSetChanged()
    }

    private fun clearButtonVisibility(s: CharSequence?): Int {
        return if (s.isNullOrEmpty()) {
            View.GONE
        } else {
            View.VISIBLE
        }
    }

    private fun updateUI(searchUIState: SearchUIState) {
        when (searchUIState) {
            SearchUIState.CONNECTION_ERROR -> {
                binding.apply {
                    progressBar.visibility = View.GONE
                    placeholderMessage.visibility = View.VISIBLE
                    placeholderMessage.text = getString(R.string.something_went_wrong)
                    nothingSearchImage.visibility = View.VISIBLE
                    nothingSearchImage.setImageResource(R.drawable.problem_search)
                    updateButton.visibility = View.VISIBLE
                }
                trackList.clear()
                trackAdapter.notifyDataSetChanged()

            }
            SearchUIState.EMPTY_SEARCH -> {
                binding.apply {
                    progressBar.visibility = View.GONE
                    placeholderMessage.visibility = View.VISIBLE
                    placeholderMessage.text = getString(R.string.nothing_found)
                    nothingSearchImage.visibility = View.VISIBLE
                    nothingSearchImage.setImageResource(R.drawable.nothing_search)
                }
                trackList.clear()
                trackAdapter.notifyDataSetChanged()
            }
            SearchUIState.LOADING -> {
                binding.apply {
                    progressBar.visibility = View.VISIBLE
                    placeholderMessage.visibility = View.GONE
                    nothingSearchImage.visibility = View.GONE
                    updateButton.visibility = View.GONE
                }
            }
            SearchUIState.SUCCESS -> {
                binding.apply {
                    placeholderMessage.visibility = View.GONE
                    nothingSearchImage.visibility = View.GONE
                    updateButton.visibility = View.GONE
                    progressBar.visibility = View.GONE
                }
            }
        }
    }

    private fun setupTextWatcher() {
        val searchTextWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (s.isNullOrEmpty()) {
                    viewModel.getSearchHistoryList()
                    handler.removeCallbacks(searchRunnable)
                }
                else {
                    binding.clearIcon.visibility = clearButtonVisibility(s)
                    showHistoryList(s, trackList)
                    handler.removeCallbacks(searchRunnable)
                    handler.postDelayed(searchRunnable, SEARCH_DEBOUNCE_DELAY)
                }

            }

            override fun afterTextChanged(s: Editable?) {

            }

        }
        binding.inputEditText.addTextChangedListener(searchTextWatcher)
    }

}

