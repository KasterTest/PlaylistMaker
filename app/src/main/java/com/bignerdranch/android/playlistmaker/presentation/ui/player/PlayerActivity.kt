package com.bignerdranch.android.playlistmaker.presentation.ui.player

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.Group
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import com.bignerdranch.android.playlistmaker.R
import com.bignerdranch.android.playlistmaker.data.PlayerRepositoryImpl
import com.bignerdranch.android.playlistmaker.domain.impl.PlayerInteractor
import com.bignerdranch.android.playlistmaker.domain.models.Track
import com.bignerdranch.android.playlistmaker.presentation.presenters.player.PlayerPresenter
import com.bignerdranch.android.playlistmaker.presentation.presenters.player.PlayerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*

class PlayerActivity : AppCompatActivity(), PlayerView {

    private lateinit var trackTitle: TextView
    private lateinit var artistsName: TextView
    private lateinit var timePlaying: TextView
    private lateinit var infoDuration: TextView
    private lateinit var infoAlbum: TextView
    private lateinit var infoYear: TextView
    private lateinit var infoStyle: TextView
    private lateinit var infoCountry: TextView
    private lateinit var albumImage: ImageView
    private lateinit var albumTextViewGroup: Group
    private  var currentTrack: Track? = null
    private lateinit var backToSearch : TextView
    private lateinit var playButton: ImageButton
    private lateinit var player: PlayerPresenter

    private val handler: Handler = Handler(Looper.getMainLooper())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.player)
        supportActionBar?.hide()
        window.statusBarColor = ContextCompat.getColor(this,
            R.color.color_status_bar_search_activity
        )
        itemAssign()
        setListener()
        val playerInteractor = PlayerInteractor(PlayerRepositoryImpl(context = applicationContext))
        player = PlayerPresenter(playerInteractor, this, handler)
        playButton.isEnabled = false
        currentTrack = player.getTrack()
        currentTrack?.let { fillPlayer(it) }
        preparePlayer()


    }
    override fun onResume() {
        super.onResume()
        currentTrack?.let { fillPlayer(it) }
    }
    override fun onPause() {
        super.onPause()
        player.pausePlayer()
    }

    override fun onDestroy() {
        super.onDestroy()
        player.playerRelease()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        player.playerStop()
    }

    private fun itemAssign () {
        trackTitle = findViewById(R.id.trackTitle)
        artistsName = findViewById(R.id.artistName)
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
        with(track) {
            trackTitle.text = trackName
            artistsName.text = artistName
            infoDuration.text = SimpleDateFormat("mm:ss", Locale.getDefault()).format(trackTimeMillis)
            albumTextViewGroup.isVisible = collectionName != "${trackName} - Single"
            infoAlbum.text = collectionName
            timePlaying.text = "00:00"
            infoYear.text = LocalDateTime.parse(releaseDate, DateTimeFormatter.ISO_DATE_TIME).year.toString()
            infoStyle.text = primaryGenreName
            infoCountry.text = country

            Glide.with(this@PlayerActivity)
                .load(artworkUrl100.replaceAfterLast('/',"512x512bb.jpg"))
                .placeholder(R.drawable.no_replay)
                .transform(RoundedCorners(20))
                .into(albumImage)
        }
    }

    private fun setListener () {
        backToSearch.setOnClickListener() {
            onBackPressed()
        }
        playButton.setOnClickListener {
            playbackControl()
        }
    }

    private fun preparePlayer() {
        playButton.isEnabled = true
        player.preparePlayer()
    }

    private fun playbackControl() {
        player.playbackControl()
    }

    override fun setButtonToPlay() {
        playButton.setImageResource(R.drawable.button_play)
    }

    override fun setButtonToPause() {
        playButton.setImageResource(R.drawable.button_pause)
    }

    override fun setStartTime() {
        timePlaying.text = "00:00"
    }

    override fun setCurrentTime() {
        timePlaying.text =
            SimpleDateFormat("mm:ss", Locale.getDefault()).format(player.getCurrentPosition())
    }

}