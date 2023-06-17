package com.bignerdranch.android.playlistmaker.medialibrary.ui.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.content.ContextCompat
import com.bignerdranch.android.playlistmaker.R
import com.bignerdranch.android.playlistmaker.databinding.ActivityMedialibraryBinding
import com.bignerdranch.android.playlistmaker.utils.router.NavigationRouter
import com.google.android.material.tabs.TabLayoutMediator

class MediaLibraryActivity : AppCompatActivity() {

    private lateinit var tabMediator: TabLayoutMediator
    private val navigationRouter by lazy { NavigationRouter(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMedialibraryBinding.inflate(layoutInflater)
        setContentView(binding.root)
        configureUI()
        binding.viewPager.adapter = MediaLibraryViewPagerAdapter(supportFragmentManager, lifecycle)

        tabMediator = TabLayoutMediator(binding.tableLayout, binding.viewPager) { tab, position ->
            when (position) {
                0 -> tab.text = getString(R.string.favorite_tracks)
                else -> tab.text = getString(R.string.playlists)
            }
        }
        tabMediator.attach()

        binding.navigationToolbar.setOnClickListener {
            navigationRouter.goBack()
        }
    }

    private fun configureUI() {
        hideSupportActionBar()
        setStatusBarColor()
    }

    private fun hideSupportActionBar() {
        supportActionBar?.hide()
    }

    private fun setStatusBarColor() {
        window.statusBarColor = ContextCompat.getColor(this, R.color.status_bar_color_media_library)
    }

    override fun onDestroy() {
        super.onDestroy()
        tabMediator.detach()
    }
}