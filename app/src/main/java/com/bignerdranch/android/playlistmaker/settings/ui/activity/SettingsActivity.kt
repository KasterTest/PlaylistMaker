package com.bignerdranch.android.playlistmaker.settings.ui.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.bignerdranch.android.playlistmaker.databinding.ActivitySettingsBinding
import com.bignerdranch.android.playlistmaker.settings.ui.view_model.SettingsViewModel
import com.bignerdranch.android.playlistmaker.utils.router.NavigationRouter


class SettingsActivity : AppCompatActivity() {

    private val binding by lazy { ActivitySettingsBinding.inflate(layoutInflater) }

    private val viewModel by lazy {
        ViewModelProvider(
            this, SettingsViewModel.getViewModelFactory()
        )[SettingsViewModel::class.java]
    }
    private val navigationRouter by lazy { NavigationRouter(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        supportActionBar?.hide()

        viewModel.observeThemeSwitcherState().observe(this) { isChecked ->
            binding.switchTheme.isChecked = isChecked
        }

        binding.apply {
            switchTheme.setOnCheckedChangeListener { _, isChecked ->
                viewModel.onThemeSwitcherClicked(isChecked)
            }

            navigationToolbar.setOnClickListener {
                navigationRouter.goBack()
            }

            buttonShare.setOnClickListener {
                viewModel.onShareAppClicked()
            }

            buttonSupport.setOnClickListener {
                viewModel.onWriteSupportClicked()
            }

            buttonAgreement.setOnClickListener {
                viewModel.termsOfUseClicked()
            }
        }
    }
}
