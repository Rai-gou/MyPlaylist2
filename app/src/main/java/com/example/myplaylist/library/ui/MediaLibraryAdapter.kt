package com.example.myplaylist.library.ui

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter

private const val NUM_TABS = 2

class MediaLibraryAdapter(fragmentActivity: FragmentActivity) : FragmentStateAdapter(fragmentActivity) {

    override fun getItemCount(): Int = NUM_TABS

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> SelectedFragment()
            1 -> PlaylistFragment()
            else -> throw IllegalArgumentException("Error: $position")
        }
    }
}