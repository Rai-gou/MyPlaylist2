package com.example.myplaylist.library.ui

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.viewpager2.widget.ViewPager2
import com.example.myplaylist.R
import com.example.myplaylist.search.data.ScreenState
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class MediaLibraryActivityViewModel(private val context: Context): ViewModel() {

    private val libraryLoadingLiveData = MutableLiveData(ScreenState())
    val getLibraryLoadingLiveData: LiveData<ScreenState> get() = libraryLoadingLiveData

    fun setupTabLayoutMediator(tabLayout: TabLayout, viewPager: ViewPager2) {
        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            when (position) {
                0 -> tab.text = context.getString(R.string.favorite_tracks)
                1 -> tab.text = context.getString(R.string.playlists)
            }
        }.attach()
    }
}