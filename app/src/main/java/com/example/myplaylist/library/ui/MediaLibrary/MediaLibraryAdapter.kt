package com.example.myplaylist.library.ui.MediaLibrary

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.myplaylist.library.ui.Playlist.PlaylistFragment
import com.example.myplaylist.library.ui.Selected.SelectedFragment

private const val NUM_TABS = 2

class MediaLibraryAdapter(fragmentActivity: MediaLibraryFragment) : FragmentStateAdapter(fragmentActivity) {
    override fun getItemCount(): Int = NUM_TABS
    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> SelectedFragment()
            1 -> PlaylistFragment()
            else -> throw IllegalArgumentException("Error: $position")
        }
    }
}