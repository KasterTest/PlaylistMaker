package com.bignerdranch.android.playlistmaker.player.ui.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import com.bignerdranch.android.playlistmaker.R
import com.bignerdranch.android.playlistmaker.databinding.PlayerBinding
import com.bignerdranch.android.playlistmaker.player.ui.models.PlayerActivityState
import com.bignerdranch.android.playlistmaker.search.domain.models.TrackModel
import com.bignerdranch.android.playlistmaker.player.ui.view_model.PlayerViewModel
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.google.gson.Gson
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*

class PlayerActivity : AppCompatActivity() {


    private val trackModel by lazy { getTrackFromBundle() }

    private val viewModel by viewModel<PlayerViewModel> {
        parametersOf(trackModel.previewUrl)
    }
    private val binding by lazy { PlayerBinding.inflate(layoutInflater) }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        supportActionBar?.hide()
        window.statusBarColor = ContextCompat.getColor(this,
            R.color.color_status_bar_search_activity
        )
        fillPlayer(trackModel)
        setListener()
        setStartTime()
        observeViewModel()

    }

    override fun onPause() {
        super.onPause()
        viewModel.pausePlayer()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        viewModel.pausePlayer()
    }


    private fun getTrackFromBundle(): TrackModel {
        return Gson().fromJson(intent.getStringExtra("TRACK_INFO"), TrackModel::class.java)
    }
    private fun observeViewModel() {
        viewModel.state.observe(this) { state ->
            when (state) {
                PlayerActivityState.StateOnComplitionTrack -> setOnComplitionTrack()
                PlayerActivityState.StatePlayerPause -> setButtonToPlay()
                PlayerActivityState.StatePlayerPlay -> setButtonToPause()
                is PlayerActivityState.StatePlayingPosition -> setCurrentTime(state.position)
                PlayerActivityState.StateTrackFavorite -> setButtonLike()
                PlayerActivityState.StateTrackUnFavorite -> setButtonUnLike()
            }

        }
    }

    fun setCurrentTime(currentPlayinTime : Int) {
        binding.timePlaying.text =
            SimpleDateFormat("mm:ss", Locale.getDefault()).format(currentPlayinTime)
    }

    private fun setListener () {
        binding.backToSearch.setOnClickListener() {
            onBackPressed()
        }
        binding.playButton.setOnClickListener {
            viewModel.playButtonClicked()
        }

        binding.buttonLike.setOnClickListener() {
            viewModel.buttonLikeClicked(trackModel)
        }
    }

    private fun fillPlayer (track: TrackModel){
        with(track) {
            if (track.isFavorite) {
                setButtonLikeImage()
            }
            else {
                setButtonUnLikeImage()
            }
            binding.trackTitle.text = trackName
            binding.artistName.text = artistName
            binding.infoDuration.text = SimpleDateFormat("mm:ss", Locale.getDefault()).format(trackTimeMillis)
            binding.albumTextViewGroup.isVisible = collectionName != "${trackName} - Single"
            binding.infoAlbum.text = collectionName
            binding.timePlaying.text = "00:00"
            binding.infoYear.text = LocalDateTime.parse(releaseDate, DateTimeFormatter.ISO_DATE_TIME).year.toString()
            binding.infoStyle.text = primaryGenreName
            binding.infoCountry.text = country

            Glide.with(this@PlayerActivity)
                .load(artworkUrl100.replaceAfterLast('/',"512x512bb.jpg"))
                .placeholder(R.drawable.no_replay)
                .transform(RoundedCorners(20))
                .into(binding.albumImage)
        }
    }

    fun setButtonToPlay() {
        binding.playButton.setImageResource(R.drawable.button_play)
    }

    fun setOnComplitionTrack(){
        binding.playButton.setImageResource(R.drawable.button_play)
        setStartTime()
    }

    fun setButtonToPause() {
        binding.playButton.setImageResource(R.drawable.button_pause)
    }

    fun setStartTime() {
        binding.timePlaying.text = "00:00"
    }

    fun setButtonLike() {
        setButtonLikeImage()
    }

    fun setButtonUnLike() {
        setButtonUnLikeImage()
    }

    private fun setButtonLikeImage() {
        binding.buttonLike.setImageResource(R.drawable.like)
        trackModel.isFavorite = true
    }

    fun setButtonUnLikeImage() {
        binding.buttonLike.setImageResource(R.drawable.unlike)
        trackModel.isFavorite = false
    }




}