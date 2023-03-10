package com.bignerdranch.android.playlistmaker

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.Group
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.google.gson.Gson
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*

class PlayerActivity : AppCompatActivity() {

    private lateinit var trackTitle: TextView
    private lateinit var artistName: TextView
    private lateinit var timePlaying: TextView
    private lateinit var infoDuration: TextView
    private lateinit var infoAlbum: TextView
    private lateinit var infoYear: TextView
    private lateinit var infoStyle: TextView
    private lateinit var infoCountry: TextView
    private lateinit var albumImage: ImageView
    private lateinit var albumTextViewGroup: Group
    private lateinit var currentTrack: Track
    private lateinit var backToSearch : TextView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.player)
        supportActionBar?.hide()
        window.statusBarColor = ContextCompat.getColor(this, R.color.color_status_bar_search_activity)
        itemAssign()
        setListener()
        currentTrack = getTrack()
        fillPlayer(currentTrack)
    }
    override fun onResume() {
        super.onResume()
        fillPlayer(currentTrack)
    }

    private fun getTrack(): Track {
        val gson = Gson()
        val serializerTrack = intent.getStringExtra(SearchActivity.TRACK_IN_PLAYER)
        return gson.fromJson(serializerTrack, Track::class.java)
    }

    private fun itemAssign () {
        trackTitle = findViewById(R.id.trackTitle)
        artistName = findViewById(R.id.artistName)
        timePlaying = findViewById(R.id.timePlaying)
        infoDuration = findViewById(R.id.info_duration)
        infoAlbum = findViewById(R.id.info_album)
        infoYear = findViewById(R.id.info_year)
        infoStyle = findViewById(R.id.info_style)
        infoCountry = findViewById(R.id.info_country)
        albumImage = findViewById(R.id.albumImage)
        albumTextViewGroup = findViewById(R.id.album_text_view_group)
        backToSearch = findViewById(R.id.back_to_search)
    }

    private fun fillPlayer (track: Track){
        trackTitle.text = track.trackName
        artistName.text = track.artistName
        timePlaying.text = "0:30" // Временно
        infoDuration.text = SimpleDateFormat("mm:ss", Locale.getDefault()).format(track.trackTimeMillis).toString()
        if (track.collectionName == "${track.trackName} - Single") {
            albumTextViewGroup.visibility = View.GONE
        }
        else {
            albumTextViewGroup.visibility = View.VISIBLE
            infoAlbum.text = track.collectionName
        }
        infoYear.text = convertDateStringToYear(track.releaseDate)
        infoStyle.text = track.primaryGenreName
        infoCountry.text = track.country
        Glide.with(this)
            .load(track.artworkUrl100.replaceAfterLast('/',"512x512bb.jpg"))
            .placeholder(R.drawable.no_replay)
            .transform(RoundedCorners(20))
            .into(albumImage)
    }

    private fun convertDateStringToYear (date: String) : String {
        val dateTime = LocalDateTime.parse(date, DateTimeFormatter.ISO_DATE_TIME)
        return dateTime.year.toString()
    }

    private fun showSearchActivity() {
        val showSearchActivity = Intent(this, SearchActivity::class.java)
        startActivity(showSearchActivity)
    }

    private fun setListener () {
        backToSearch.setOnClickListener() {
          showSearchActivity()
        }

    }

}