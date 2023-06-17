package com.bignerdranch.android.playlistmaker.settings.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.bignerdranch.android.playlistmaker.settings.ui.view_model.SettingsViewModel
import com.bignerdranch.android.playlistmaker.databinding.FragmentSettingsBinding
import org.koin.androidx.viewmodel.ext.android.viewModel


class SettingsFragment :  Fragment() {

    private lateinit var binding: FragmentSettingsBinding
    private val viewModel by viewModel<SettingsViewModel>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentSettingsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.observeThemeSwitcherState().observe(viewLifecycleOwner) { isChecked ->
            binding.switchTheme.isChecked = isChecked
        }

        binding.apply {
            switchTheme.setOnCheckedChangeListener { _, isChecked ->
                viewModel.onThemeSwitcherClicked(isChecked)
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
