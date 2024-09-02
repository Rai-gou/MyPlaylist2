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

open class NewPlaylistViewModel(
    val playlistInteractor: PlaylistInteractor
) : ViewModel() {

    open val _playlistId = MutableLiveData<String>()
    open val playlistId: LiveData<String> get() = _playlistId

    val _playlistName = MutableLiveData<String>()
    val playlistName: LiveData<String> get() = _playlistName

    val _playlistDescription = MutableLiveData<String>()
    val playlistDescription: LiveData<String> get() = _playlistDescription

    val _playlistImageUri = MutableLiveData<Uri?>()
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

    fun onPlaylistDescriptionChanged(description: String?) {
        val nonNullDescription = description ?: ""
        _playlistDescription.value = nonNullDescription
    }

    fun onImageSelected(uri: Uri) {
        _playlistImageUri.value = uri
    }

    fun onPlaylistNewName(newName: String) {
        if (_playlistName.value != newName) {
            _playlistName.value = newName
        }
    }

    fun onPlaylistNewDescription(newDescription: String) {
        if (_playlistDescription.value != newDescription) {
            _playlistDescription.value = newDescription
        }
    }

    fun onImageNewSelected(uri: Uri) {
        if (_playlistImageUri.value != uri) {
            _playlistImageUri.value = uri
        }
    }

    fun createPlaylist() {
        val name = _playlistName.value
        val description = _playlistDescription.value ?: ""
        val uri = _playlistImageUri.value
        viewModelScope.launch {
            if (name != null) {
                playlistInteractor.createPlaylist(name, description, uri)
            }
        }
    }
}