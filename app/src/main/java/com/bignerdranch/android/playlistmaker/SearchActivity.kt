package com.bignerdranch.android.playlistmaker

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.EditText
import android.widget.ImageView



class SearchActivity : AppCompatActivity() {

    companion object {
        const val SEARCH_STRING = "SEARCH_STRING"
    }
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
        val inputSearchEditText = findViewById<EditText>(R.id.inputEditText)
        val clearButton = findViewById<ImageView>(R.id.clearIcon)
        clearButton.setOnClickListener {
            inputSearchEditText.setText("")
        }
        val searchTextWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // empty
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
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

}

