package com.bignerdranch.android.playlistmaker

import android.os.Bundle
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
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class SearchActivity : AppCompatActivity() {

    companion object {
        const val SEARCH_STRING = "SEARCH_STRING"
    }

    private val itunesBaseUrl = "https://itunes.apple.com"
    private lateinit var placeholderMessage: TextView
    private lateinit var inputSearchEditText: TextView
    private lateinit var nothingSearchImage: ImageView
    private lateinit var updateButton: Button
    private lateinit var clearButton : ImageView
    private lateinit var labelSearch : TextView
    private lateinit var clearHistory : Button

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

        val searchHistory = SearchHistory(getSharedPreferences(SEARCH_HISTORY_KEY, MODE_PRIVATE))

        val recyclerView = findViewById<RecyclerView>(R.id.recycler_view)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)


        adapter.itemClickListener = {position, track ->
            searchHistory.addTrack(track)
        }


        setListener(searchHistory)

        val searchTextWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                goneHistorySearch(s, searchHistory)
                clearButton.visibility = clearButtonVisibility(s)
                putInputSearchEditText = findViewById<EditText>(R.id.inputEditText).text.toString()

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
            itunesService.findTrack(inputSearchEditText.text.toString()).enqueue(object :
                Callback<ItunesResponse> {
                override fun onResponse(call: Call<ItunesResponse>,
                                        response: Response<ItunesResponse>
                ) {
                    if (response.code() == 200) {
                        trackList.clear()
                        if (response.body()?.results?.isNotEmpty() == true) {
                            trackList.addAll(response.body()?.results!!)
                            adapter.notifyDataSetChanged()
                        }
                        if (trackList.isEmpty()) {
                            displayNothingFound()
                        } else {
                            hideProblemView()
                        }
                    } else {
                        displayInetProblem()
                    }
                }

                override fun onFailure(call: Call<ItunesResponse>, t: Throwable) {
                    displayInetProblem()
                }

            })
        }
    }
    private fun displayInetProblem (){
        placeholderMessage.visibility = View.VISIBLE
        placeholderMessage.text = getString(R.string.something_went_wrong)
        nothingSearchImage.visibility = View.VISIBLE
        nothingSearchImage.setImageResource(R.drawable.problem_search)
        updateButton.visibility = View.VISIBLE
        trackList.clear()
        adapter.notifyDataSetChanged()
    }
    private fun displayNothingFound () {
        placeholderMessage.visibility = View.VISIBLE
        placeholderMessage.text = getString(R.string.nothing_found)
        nothingSearchImage.visibility = View.VISIBLE
        nothingSearchImage.setImageResource(R.drawable.nothing_search)
        trackList.clear()
        adapter.notifyDataSetChanged()
    }

    private fun hideProblemView () {
        placeholderMessage.visibility = View.GONE
        placeholderMessage.visibility = View.GONE
        nothingSearchImage.visibility = View.GONE
        updateButton.visibility = View.GONE
    }

    private fun itemAssign () {

        nothingSearchImage = findViewById(R.id.nothing_search_image)
        placeholderMessage = findViewById(R.id.placeholderMessage)
        updateButton = findViewById(R.id.update_button)
        inputSearchEditText = findViewById<EditText>(R.id.inputEditText)
        clearButton = findViewById(R.id.clearIcon)
        labelSearch = findViewById(R.id.label_search)
        clearHistory = findViewById(R.id.clear_history)
    }

    private fun setListener (searchHistory: SearchHistory) {
        updateButton.setOnClickListener {
            sendResponse()
        }

        // временный слушатель (потом уберем)
        inputSearchEditText.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                sendResponse()
            }
            false

        }

        clearButton.setOnClickListener {
            inputSearchEditText.text = ""
            inputSearchEditText.onEditorAction(EditorInfo.IME_ACTION_DONE)
            inputSearchEditText.isFocusable = false
            inputSearchEditText.isFocusableInTouchMode = false
            inputSearchEditText.isFocusable = true
            inputSearchEditText.isFocusableInTouchMode = true
            goneHistorySearch(" ", searchHistory)

        }

        inputSearchEditText.setOnFocusChangeListener { view, hasFocus ->
            inputSearchEditText.isFocusable = true
            inputSearchEditText.isFocusableInTouchMode = true
            goneHistorySearch(inputSearchEditText.text, searchHistory)
        }

        clearHistory.setOnClickListener {
            searchHistory.clearSearchHistory()
            goneHistorySearch(inputSearchEditText.text, searchHistory)
        }
    }



    private fun goneHistorySearch (s: CharSequence?, searchHistory: SearchHistory) {
        if (s?.isEmpty() == true && searchHistory.getSearchHistoryList().isNotEmpty()) {
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
}




