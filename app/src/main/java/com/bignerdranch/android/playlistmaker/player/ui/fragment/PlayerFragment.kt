package com.bignerdranch.android.playlistmaker.player.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.ImageButton
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.bignerdranch.android.playlistmaker.R
import com.bignerdranch.android.playlistmaker.databinding.PlayerBinding
import com.bignerdranch.android.playlistmaker.player.ui.models.PlayerActivityState
import com.bignerdranch.android.playlistmaker.search.domain.models.TrackModel
import com.bignerdranch.android.playlistmaker.player.ui.view_model.PlayerViewModel
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.google.gson.Gson
import com.practicum.playlistmaker.library.ui.bottom_sheet.BottomSheet
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*

class PlayerFragment : Fragment() {

    private lateinit var binding: PlayerBinding
    private val trackModel by lazy { retrieveTrack() }
    private val viewModel by viewModel<PlayerViewModel> {
        parametersOf(trackModel.previewUrl)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = PlayerBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fillPlayer(trackModel)
        setListener()
        setStartTime()
        observeViewModel()

    }

    override fun onPause() {
        super.onPause()
        viewModel.pausePlayer()
    }

    fun retrieveTrack(): TrackModel {
        val trackJson = requireArguments().getString(KEY_TRACK)
        return Gson().fromJson(trackJson, TrackModel::class.java) ?: TrackModel.emptyTrack
    }

    private fun observeViewModel() {
        viewModel.state.observe(viewLifecycleOwner) { state ->
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

    private fun startAnimation(button: ImageButton) {
        button.startAnimation(
            AnimationUtils.loadAnimation(
                requireContext(), R.anim.scale
            )
        )
    }

    private fun setListener () {
        binding.backToSearch.setOnClickListener() {
            findNavController().navigateUp()
        }
        binding.playButton.setOnClickListener {
            viewModel.playButtonClicked()
        }

        binding.buttonLike.setOnClickListener() {
            viewModel.buttonLikeClicked(trackModel)
        }

        binding.addButton.setOnClickListener { button ->
            (button as? ImageButton)?.let { startAnimation(it) }
            findNavController().navigate(
                R.id.action_playerFragment_to_bottomSheet, BottomSheet.createArgs(trackModel)
            )
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

            Glide.with(this@PlayerFragment)
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

    companion object {
        const val KEY_TRACK = "track"
        private const val TRANSITION_DURATION = 1000

        fun createArgs(track: TrackModel): Bundle {
            val gson = Gson()
            val jsonTrack = gson.toJson(track)
            val bundle = Bundle()
            bundle.putString(KEY_TRACK, jsonTrack)
            return bundle
        }
    }






}