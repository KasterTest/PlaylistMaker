package com.bignerdranch.android.playlistmaker.medialibrary.ui.root_fragment

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.bignerdranch.android.playlistmaker.medialibrary.ui.child_fragment.FavoriteTracksFragment
import com.bignerdranch.android.playlistmaker.medialibrary.ui.child_fragment.PlaylistsFragment

class MediaLibraryViewPagerAdapter (fragmentManager: FragmentManager, lifecycle: Lifecycle) :
    FragmentStateAdapter(fragmentManager, lifecycle) {
    override fun getItemCount(): Int {
        return 2
    }

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> FavoriteTracksFragment.newInstance()
            else -> PlaylistsFragment.newInstance()
        }
    }

}