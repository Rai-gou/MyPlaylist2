package com.example.myplaylist.library.ui.EditPlaylist

import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.myplaylist.R
import com.example.myplaylist.databinding.FragmentNewPlaylistBinding
import com.example.myplaylist.library.domain.PlaylistInteractor
import com.example.myplaylist.library.ui.NewPlaylist.NewPlaylistFragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import org.koin.android.ext.android.inject

class EditPlaylistFragment : NewPlaylistFragment() {

    private val playlistInteractor: PlaylistInteractor by inject()
    private val editViewModel: EditPlaylistViewModel by viewModels {
        EditPlaylistViewModelFactory(playlistInteractor)
    }

    private var currentImageUri: Uri? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        requireActivity().findViewById<BottomNavigationView>(R.id.bottomNavigationView).visibility =
            View.GONE

        binding.buttonNewPlaylist.text = getString(R.string.save)
        binding.newPlaylistView.text = getString(R.string.edit)

        val playlistId = arguments?.getString("playlistId")
        playlistId?.let {
            editViewModel.loadPlaylist(it)
        }

        // Настройка наблюдателей
        editViewModel.playlistName.observe(viewLifecycleOwner, nameObserver)
        editViewModel.playlistDescription.observe(viewLifecycleOwner, descriptionObserver)
        editViewModel.playlistImageUri.observe(viewLifecycleOwner, imageObserver)

        binding.buttonNewPlaylist.setOnClickListener {
            if (currentImageUri == null) {
                currentImageUri = editViewModel.playlistImageUri.value
            }
            editViewModel.savePlaylist {
                parentFragmentManager.popBackStackImmediate()
            }
        }

        binding.inputEditNamePlaylist.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                editViewModel.onPlaylistNewName(s.toString())
            }
            override fun afterTextChanged(s: Editable?) {}
        })

        binding.inputEditDescriptionPlaylist.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                editViewModel.onPlaylistNewDescription(s.toString())
            }
            override fun afterTextChanged(s: Editable?) {}
        })

        val pickMedia = registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
            if (uri != null) {
                loadImageWithRoundedCorners(uri)
                currentImageUri = uri
                editViewModel.onImageNewSelected(uri)
            } else {
                Log.d("EditPlaylistFragment", "No media selected")
                loadImageWithRoundedCorners(R.drawable.playplaceholder) // Показываем плейсхолдер
            }
        }
        binding.imagePlayer.setOnClickListener {
            pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
        }
    }

    private fun loadImageWithRoundedCorners(imageSource: Any) {
        Glide.with(this)
            .load(imageSource)
            .transform(RoundedCorners(8))
            .placeholder(R.drawable.playplaceholder) // Плейсхолдер при загрузке
            .error(R.drawable.playplaceholder) // Плейсхолдер при ошибке
            .into(binding.imagePlayer)
    }

    override fun onResume() {
        super.onResume()
        requireActivity().findViewById<BottomNavigationView>(R.id.bottomNavigationView).visibility =
            View.GONE
    }
}