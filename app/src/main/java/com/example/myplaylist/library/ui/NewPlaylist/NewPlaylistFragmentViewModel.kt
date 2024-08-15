package com.example.myplaylist.library.ui.NewPlaylist

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myplaylist.library.domain.PlaylistInteractor
import com.example.myplaylist.library.ui.Selected.ScreenStateNewPlaylist
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class NewPlaylistViewModel(
    private val playlistInteractor: PlaylistInteractor
) : ViewModel() {

    private val _playlistName = MutableLiveData<String>()
    val playlistName: LiveData<String> get() = _playlistName

    private val _playlistDescription = MutableLiveData<String>()
    val playlistDescription: LiveData<String> get() = _playlistDescription

    private val _playlistImageUri = MutableLiveData<Uri?>()
    val playlistImageUri: LiveData<Uri?> get() = _playlistImageUri

    private val _isDataChanged = MutableLiveData<Boolean>()
    val isDataChanged: LiveData<Boolean> get() = _isDataChanged

    private val _playlistNameChanged = MutableLiveData<Boolean>()
    val playlistNameChanged: LiveData<Boolean> get() = _playlistNameChanged

    private val _screenState = MutableStateFlow(ScreenStateNewPlaylist())
    val screenState: StateFlow<ScreenStateNewPlaylist> get() = _screenState.asStateFlow()

    init {
        val dataChangedObserver = Observer<Any> {
            _isDataChanged.value = !_playlistName.value.isNullOrEmpty() ||
                    !_playlistDescription.value.isNullOrEmpty() ||
                    _playlistImageUri.value != null
        }
        val nameChangedObserver = Observer<String> {
            _playlistNameChanged.value = !_playlistName.value.isNullOrEmpty()
        }
        _playlistName.observeForever(dataChangedObserver as Observer<String>)
        _playlistDescription.observeForever(dataChangedObserver as Observer<String>)
        _playlistImageUri.observeForever(dataChangedObserver as Observer<Uri?>)
        _playlistName.observeForever(nameChangedObserver)
    }
    fun onPlaylistNameChanged(name: String) {
        _playlistName.value = name
    }

    fun onPlaylistDescriptionChanged(description: String) {
        _playlistDescription.value = description
    }

    fun onImageSelected(uri: Uri) {
        _playlistImageUri.value = uri
    }

    fun createPlaylist() {
        val name = _playlistName.value
        val uri = _playlistImageUri.value
        viewModelScope.launch {
            if (name != null) {
                playlistInteractor.createPlaylist(name, uri)
            }
        }
    }
}