package com.bignerdranch.android.playlistmaker.main.ui.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.WindowManager
import com.bignerdranch.android.playlistmaker.medialibrary.ui.activity.MediaLibraryActivity
import com.bignerdranch.android.playlistmaker.databinding.ActivityMainBinding
import com.bignerdranch.android.playlistmaker.main.ui.models.NavigationState
import com.bignerdranch.android.playlistmaker.main.ui.view_model.MainViewModel
import com.bignerdranch.android.playlistmaker.search.ui.activity.SearchActivity
import com.bignerdranch.android.playlistmaker.settings.ui.activity.SettingsActivity
import org.koin.androidx.viewmodel.ext.android.viewModel


class MainActivity : AppCompatActivity() {

    private val viewModel by viewModel<MainViewModel>()
    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        setContentView(binding.root)
        supportActionBar?.hide()
        actionBar?.hide()

        viewModel.observeContentStateLiveData().observe(this) { navigationState ->
            navigation(navigationState)
        }
        setupListeners()
    }

    private fun setupListeners() {

        binding.mainButtonSearch.setOnClickListener {
            viewModel.onSearchButtonClicked()
        }
        binding.mainButtonMediaLibrary.setOnClickListener {
            viewModel.onLibraryButtonClicked()
        }
        binding.mainButtonSettings.setOnClickListener {
            viewModel.onSettingsButtonClicked()
        }
    }

    private fun navigation(state: NavigationState) {
        when (state) {
            NavigationState.Search -> {
                startActivity(Intent(this, SearchActivity::class.java))
            }
            NavigationState.Library -> {
                startActivity(Intent(this, MediaLibraryActivity::class.java))
            }
            NavigationState.Settings -> {
                startActivity(Intent(this, SettingsActivity::class.java))
            }
        }
    }
}



