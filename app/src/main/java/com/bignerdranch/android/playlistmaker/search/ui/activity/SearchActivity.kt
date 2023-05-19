package com.bignerdranch.android.playlistmaker.search.ui.activity

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.bignerdranch.android.playlistmaker.*
import com.bignerdranch.android.playlistmaker.databinding.ActivitySearchBinding
import com.bignerdranch.android.playlistmaker.search.data.storage.TrackSearchState
import com.bignerdranch.android.playlistmaker.search.domain.models.TrackModel
import com.bignerdranch.android.playlistmaker.search.ui.models.SearchUIState
import com.bignerdranch.android.playlistmaker.search.ui.view_model.SearchViewModel
import com.bignerdranch.android.playlistmaker.utils.router.NavigationRouter



class SearchActivity : AppCompatActivity() {

    companion object {
        const val SEARCH_STRING = "SEARCH_STRING"
        private const val SEARCH_DEBOUNCE_DELAY = 2000L
        private const val CLICK_DEBOUNCE_DELAY = 1000L
    }

    private val viewModel by lazy { getViewModel() }
    private val navigationRouter by lazy { NavigationRouter(this) }
    private val handler = Handler(Looper.getMainLooper())
    private val searchRunnable = Runnable { viewModel.searchTracks(binding.inputEditText.text.toString()) }
    private var isClickAllowed = true
    private lateinit var binding: ActivitySearchBinding
    private val trackList = ArrayList<TrackModel>()
    private val trackAdapter = TrackAdapter(trackList)

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(SEARCH_STRING, binding.inputEditText.text.toString())
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        binding.inputEditText.setText(savedInstanceState.getString(SEARCH_STRING, ""))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)
        configureUI()
        setupListeners()
        observeViewModel()
    }

    override fun onDestroy() {
        super.onDestroy()
        navigationRouter.activity = null
    }

    private fun configureUI() {
        hideSupportActionBar()
        setStatusBarColor()
        setupRecyclerView()
        setupTextWatcher()
    }

    private fun hideSupportActionBar() {
        supportActionBar?.hide()
    }

    private fun setStatusBarColor() {
        window.statusBarColor = ContextCompat.getColor(this, R.color.color_status_bar_search_activity)
    }

    private fun setupRecyclerView() {
        binding.recyclerView.apply {
            adapter = trackAdapter
            layoutManager = LinearLayoutManager(context)
        }
    }

    private fun observeViewModel() {
        viewModel.trackSearchState.observe(this) { state ->
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
        binding.backToMain.setOnClickListener { onBackPressed() }

        binding.clearHistory.setOnClickListener {
            handler.removeCallbacks(searchRunnable)
            viewModel.clearSearchHistory()
            viewModel.getSearchHistoryList()
        }

        binding.updateButton.setOnClickListener {
            viewModel.searchTracks(binding.inputEditText.text.toString())
        }


        trackAdapter.itemClickListener = {position, track ->
            if (isClickAllowed) {
                isClickAllowed = false
                handler.postDelayed({ isClickAllowed = true }, CLICK_DEBOUNCE_DELAY)
                viewModel.addTrackToSearchHistory(track)
                navigationRouter.openAudioPlayer(track)
            }
        }

    }


    @JvmName("getViewModel1")
    private fun getViewModel(): SearchViewModel {
        return ViewModelProvider(this, SearchViewModel.getViewModelFactory())[SearchViewModel::class.java]
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

