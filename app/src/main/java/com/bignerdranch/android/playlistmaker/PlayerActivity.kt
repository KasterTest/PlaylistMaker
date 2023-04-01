package com.bignerdranch.android.playlistmaker

import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.ImageButton
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

    companion object {
        private const val STATE_DEFAULT = 0
        private const val STATE_PREPARED = 1
        private const val STATE_PLAYING = 2
        private const val STATE_PAUSED = 3
        private const val UPDATE_DEBOUNCE_DELAY = 400L
    }

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
    private lateinit var playButton: ImageButton
    private lateinit var mediaPlayer: MediaPlayer
    private var playerState = STATE_DEFAULT
    private val handler = Handler(Looper.getMainLooper())
    private val playingTimeRunnable = Runnable { updatePlayingTime() }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.player)
        supportActionBar?.hide()
        window.statusBarColor = ContextCompat.getColor(this, R.color.color_status_bar_search_activity)
        itemAssign()
        setListener()
        currentTrack = getTrack()
        fillPlayer(currentTrack)
        mediaPlayer = MediaPlayer()
        playButton.isEnabled = false
        preparePlayer(currentTrack.previewUrl)
    }
    override fun onResume() {
        super.onResume()
        fillPlayer(currentTrack)
    }
    override fun onPause() {
        super.onPause()
        pausePlayer()
    }

    override fun onDestroy() {
        super.onDestroy()
        mediaPlayer.release()
        handler.removeCallbacks(playingTimeRunnable)
    }

    override fun onBackPressed() {
        super.onBackPressed()
        mediaPlayer.stop()
        handler.removeCallbacks(playingTimeRunnable)
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
        playButton = findViewById(R.id.playButton)
    }

    private fun fillPlayer (track: Track){
        trackTitle.text = track.trackName
        artistName.text = track.artistName
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

    private fun setListener () {
        backToSearch.setOnClickListener() {
            onBackPressed()
        }
        playButton.setOnClickListener {
            playbackControl()
        }
    }

    private fun preparePlayer(previewUrl: String) {
        mediaPlayer.setDataSource(previewUrl)
        mediaPlayer.prepareAsync()
        mediaPlayer.setOnPreparedListener {
            playButton.isEnabled = true
            playerState = STATE_PREPARED
        }
        mediaPlayer.setOnCompletionListener {
            playButton.setImageResource(R.drawable.button_play)
            playerState = STATE_PREPARED
            handler.removeCallbacks(playingTimeRunnable)
            timePlaying.text = "00:00"
        }
    }
    private fun startPlayer() {
        mediaPlayer.start()
        playButton.setImageResource(R.drawable.button_pause)
        playerState = STATE_PLAYING
        updatePlayingDebounce()
    }

    private fun pausePlayer() {
        mediaPlayer.pause()
        playButton.setImageResource(R.drawable.button_play)
        playerState = STATE_PAUSED

    }

    private fun playbackControl() {
        when(playerState) {
            STATE_PLAYING -> {
                pausePlayer()
                handler.removeCallbacks(playingTimeRunnable)
            }
            STATE_PREPARED, STATE_PAUSED -> {
                startPlayer()
            }
        }
    }
    private fun  updatePlayingTime(){
        timePlaying.text = SimpleDateFormat("mm:ss", Locale.getDefault()).format(mediaPlayer.currentPosition).toString()
        handler.postDelayed(playingTimeRunnable, UPDATE_DEBOUNCE_DELAY)
    }

    private fun updatePlayingDebounce() {
        handler.removeCallbacks(playingTimeRunnable)
        handler.postDelayed(playingTimeRunnable, UPDATE_DEBOUNCE_DELAY)
    }
}