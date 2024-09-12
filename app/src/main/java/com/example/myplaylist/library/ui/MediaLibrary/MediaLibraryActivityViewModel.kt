package com.example.myplaylist.library.ui.MediaLibrary

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.myplaylist.search.data.ScreenState

class MediaLibraryActivityViewModel(private val context: Context) : ViewModel() {

    private val libraryLoadingLiveData = MutableLiveData(ScreenState())

}