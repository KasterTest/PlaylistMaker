package com.bignerdranch.android.playlistmaker

import android.content.Intent
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
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bignerdranch.android.playlistmaker.SearchHistory.Companion.SEARCH_HISTORY_KEY
import com.bignerdranch.android.playlistmaker.domain.models.Track
import com.bignerdranch.android.playlistmaker.presentation.ui.player.PlayerActivity
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class SearchActivity : AppCompatActivity() {

    companion object {
        const val SEARCH_STRING = "SEARCH_STRING"
        const val TRACK_IN_PLAYER = "TRACK_IN_PLAYER"
        const val KEY_TRACK = "KEY_TRACK"
        private const val SEARCH_DEBOUNCE_DELAY = 2000L
        private const val CLICK_DEBOUNCE_DELAY = 1000L
    }

    private val itunesBaseUrl = "https://itunes.apple.com"
    private lateinit var placeholderMessage: TextView
    private lateinit var inputSearchEditText: TextView
    private lateinit var nothingSearchImage: ImageView
    private lateinit var updateButton: Button
    private lateinit var clearButton : ImageView
    private lateinit var labelSearch : TextView
    private lateinit var clearHistory : Button
    private lateinit var searchHistory: SearchHistory
    private lateinit var progressBar: ProgressBar
    private val handler = Handler(Looper.getMainLooper())
    private val searchRunnable = Runnable { sendResponse() }
    private var isClickAllowed = true


    private val retrofit = Retrofit.Builder()
        .baseUrl(itunesBaseUrl)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val itunesService = retrofit.create(ItunesSearchApi::class.java)
    private val trackList = ArrayList<Track>()
    val adapter = TrackAdapter(trackList)

    var putInputSearchEditText = ""
    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(SEARCH_STRING, putInputSearchEditText)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        val inputSearchEditText = findViewById<EditText>(R.id.inputEditText)
        inputSearchEditText.setText(savedInstanceState.getString(SEARCH_STRING,""))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)
        supportActionBar?.hide()
        window.statusBarColor = ContextCompat.getColor(this, R.color.color_status_bar_search_activity)
        itemAssign()

        val recyclerView = findViewById<RecyclerView>(R.id.recycler_view)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)

        adapter.itemClickListener = {position, track ->
            if (clickDebounce()) {
            searchHistory.addTrack(track)
            showPlayer(track)
            }
        }


        setListener()

        val searchTextWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                goneHistorySearch(s, searchHistory)
                clearButton.visibility = clearButtonVisibility(s)
                putInputSearchEditText = findViewById<EditText>(R.id.inputEditText).text.toString()
                searchDebounce()

            }
            override fun afterTextChanged(s: Editable?) {

            }

            private fun clearButtonVisibility(s: CharSequence?): Int {
                return if (s.isNullOrEmpty()) {
                    View.GONE
                } else {
                    View.VISIBLE
                }
            }
        }
        inputSearchEditText.addTextChangedListener(searchTextWatcher)
    }

    private fun sendResponse() {
        if (inputSearchEditText.text.isNotEmpty()) {
            updateUI(SearchStatus.LOADING)
            itunesService.findTrack(inputSearchEditText.text.toString()).enqueue(object :
                Callback<ItunesResponse> {
                override fun onResponse(call: Call<ItunesResponse>,
                                        response: Response<ItunesResponse>) {
                    if (response.code() == 200) {
                        trackList.clear()
                        if (response.body()?.results?.isNotEmpty() == true) {
                            trackList.addAll(response.body()?.results!!)
                            adapter.notifyDataSetChanged()
                            updateUI(SearchStatus.SUCCESS)
                        } else {
                            updateUI(SearchStatus.EMPTY_SEARCH)
                        }
                    } else {
                        updateUI(SearchStatus.CONNECTION_ERROR)
                    }
                }

                override fun onFailure(call: Call<ItunesResponse>, t: Throwable) {
                    updateUI(SearchStatus.CONNECTION_ERROR)
                }
            })
        }
    }

    private fun updateUI(searchStatus: SearchStatus) {
        when (searchStatus) {
            SearchStatus.CONNECTION_ERROR -> {
                progressBar.visibility = View.GONE
                placeholderMessage.visibility = View.VISIBLE
                placeholderMessage.text = getString(R.string.something_went_wrong)
                nothingSearchImage.visibility = View.VISIBLE
                nothingSearchImage.setImageResource(R.drawable.problem_search)
                updateButton.visibility = View.VISIBLE
                trackList.clear()
                adapter.notifyDataSetChanged()
            }
            SearchStatus.EMPTY_SEARCH -> {
                progressBar.visibility = View.GONE
                placeholderMessage.visibility = View.VISIBLE
                placeholderMessage.text = getString(R.string.nothing_found)
                nothingSearchImage.visibility = View.VISIBLE
                nothingSearchImage.setImageResource(R.drawable.nothing_search)
                trackList.clear()
                adapter.notifyDataSetChanged()
            }
            SearchStatus.SUCCESS -> {
                placeholderMessage.visibility = View.GONE
                nothingSearchImage.visibility = View.GONE
                updateButton.visibility = View.GONE
                progressBar.visibility = View.GONE
            }
            SearchStatus.LOADING -> {
                placeholderMessage.visibility = View.GONE
                nothingSearchImage.visibility = View.GONE
                updateButton.visibility = View.GONE
                progressBar.visibility = View.VISIBLE
            }
        }
    }

    private fun searchDebounce() {
        handler.removeCallbacks(searchRunnable)
        handler.postDelayed(searchRunnable, SEARCH_DEBOUNCE_DELAY)
    }

    private fun clickDebounce() : Boolean {
        val current = isClickAllowed
        if (isClickAllowed) {
            isClickAllowed = false
            handler.postDelayed({ isClickAllowed = true }, CLICK_DEBOUNCE_DELAY)
        }
        return current
    }

    private fun itemAssign () {
        nothingSearchImage = findViewById(R.id.nothing_search_image)
        placeholderMessage = findViewById(R.id.placeholderMessage)
        updateButton = findViewById(R.id.update_button)
        inputSearchEditText = findViewById<EditText>(R.id.inputEditText)
        clearButton = findViewById(R.id.clearIcon)
        labelSearch = findViewById(R.id.label_search)
        clearHistory = findViewById(R.id.clear_history)
        searchHistory = SearchHistory(getSharedPreferences(SEARCH_HISTORY_KEY, MODE_PRIVATE))
        progressBar = findViewById(R.id.progressBar)
    }

    private fun setListener () {
        updateButton.setOnClickListener {
            sendResponse()
        }

        inputSearchEditText.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                sendResponse()
            }
            false

        }

        clearButton.setOnClickListener {
            inputSearchEditText.text = ""
            inputSearchEditText.onEditorAction(EditorInfo.IME_ACTION_DONE)
            inputSearchEditText.clearFocus()
            inputSearchEditText.isFocusable = true
            inputSearchEditText.isFocusableInTouchMode = true
            goneHistorySearch(" ", searchHistory)
            updateUI(SearchStatus.SUCCESS)

        }

        inputSearchEditText.setOnFocusChangeListener { view, hasFocus ->
            inputSearchEditText.isFocusable = true
            inputSearchEditText.isFocusableInTouchMode = true
            goneHistorySearch(inputSearchEditText.text, searchHistory)
            updateUI(SearchStatus.SUCCESS)
        }

        clearHistory.setOnClickListener {
            searchHistory.clearSearchHistory()
            goneHistorySearch(inputSearchEditText.text, searchHistory)
        }
    }



    private fun goneHistorySearch (s: CharSequence?, searchHistory: SearchHistory) {
        if (s?.isEmpty() == true && searchHistory.getSearchHistoryList().isNotEmpty()) {
            updateUI(SearchStatus.SUCCESS)
            labelSearch.visibility = View.VISIBLE
            clearHistory.visibility = View.VISIBLE
            trackList.clear()
            trackList.addAll(searchHistory.getSearchHistoryList())
            adapter.notifyDataSetChanged()
        } else {
            labelSearch.visibility = View.GONE
            clearHistory.visibility = View.GONE
            trackList.clear()
            adapter.notifyDataSetChanged()

        }
    }

    private fun showPlayer(track: Track) {
        val displayPlayerActivityIntent = Intent(this, PlayerActivity::class.java)
        val gson = Gson()
        val serializerTrack = gson.toJson(track)
        getSharedPreferences(TRACK_IN_PLAYER, MODE_PRIVATE).edit().putString(KEY_TRACK, serializerTrack).apply()
        startActivity(displayPlayerActivityIntent)
    }
}

