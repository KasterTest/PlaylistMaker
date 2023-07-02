package com.bignerdranch.android.playlistmaker.medialibrary.ui.root_fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.bignerdranch.android.playlistmaker.R
import com.bignerdranch.android.playlistmaker.databinding.FragmentMedialibraryBinding
import com.google.android.material.tabs.TabLayoutMediator


class MediaLibraryFragment : Fragment() {
    private lateinit var binding: FragmentMedialibraryBinding
    private lateinit var tabMediator: TabLayoutMediator

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentMedialibraryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.viewPager.adapter = MediaLibraryViewPagerAdapter(childFragmentManager, lifecycle)

        tabMediator = TabLayoutMediator(binding.tableLayout, binding.viewPager) { tab, position ->
            when (position) {
                0 -> tab.text = getString(R.string.favorite_tracks)
                else -> tab.text = getString(R.string.playlists)
            }
        }
        tabMediator.attach()

    }
    override fun onDestroyView() {
        super.onDestroyView()
        tabMediator.detach()
    }
}