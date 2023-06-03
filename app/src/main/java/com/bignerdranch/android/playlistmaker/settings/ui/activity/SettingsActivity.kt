package com.bignerdranch.android.playlistmaker.settings.ui.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bignerdranch.android.playlistmaker.databinding.ActivitySettingsBinding
import com.bignerdranch.android.playlistmaker.settings.ui.view_model.SettingsViewModel
import com.bignerdranch.android.playlistmaker.utils.router.NavigationRouter
import org.koin.androidx.viewmodel.ext.android.viewModel


class SettingsActivity : AppCompatActivity() {

    private val binding by lazy { ActivitySettingsBinding.inflate(layoutInflater) }

    private val viewModel by viewModel<SettingsViewModel>()
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
