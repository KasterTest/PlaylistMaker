package com.bignerdranch.android.playlistmaker.core.root

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.bignerdranch.android.playlistmaker.R
import com.bignerdranch.android.playlistmaker.databinding.ActivityRootBinding

class RootActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRootBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRootBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.container_view) as NavHostFragment
        val navController = navHostFragment.navController

        binding.bottomNavigationView.setupWithNavController(navController)

        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.playerFragment, R.id.newPlaylistFragment, R.id.bottomSheet -> {
                    binding.bottomNavigationView.visibility = View.GONE
                }

                else -> {
                    binding.bottomNavigationView.visibility = View.VISIBLE }
            }
        }
    }


    fun animateBottomNavigationView() {
        binding.bottomNavigationView.visibility = View.GONE
    }

    override fun onResume() {
        super.onResume()
        binding.bottomNavigationView.visibility = View.VISIBLE
    }
}