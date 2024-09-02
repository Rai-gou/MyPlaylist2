package com.example.myplaylist.library.ui.EditPlaylist

import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.viewModels
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.example.myplaylist.R
import com.example.myplaylist.library.data.NewPlaylistWithTracks
import com.example.myplaylist.library.domain.PlaylistInteractor
import com.example.myplaylist.library.ui.NewPlaylist.NewPlaylistFragment
import com.example.myplaylist.main.ui.RootActivity
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

        binding.buttonNewPlaylist.text = getString(R.string.save)
        binding.newPlaylistView.text = getString(R.string.edit)

        (requireActivity() as? RootActivity)?.setBottomNavigationVisibility(false)

        val playlistId = arguments?.getString("playlistId")
        playlistId?.let {
            editViewModel.loadPlaylist(it)
        } ?: run {
            Toast.makeText(context, "Playlist ID is missing", Toast.LENGTH_SHORT).show()
        }
        editViewModel.playlistWithTracks.observe(viewLifecycleOwner) { playlistWithTracks ->
            playlistWithTracks?.let {
                setupPlaylistDetails(it)
            }
        }

        binding.buttonNewPlaylist.setOnClickListener {
            if (currentImageUri == null) {
                currentImageUri = editViewModel.playlistImageUri.value
            }
            editViewModel.savePlaylist {
                checkNavController()
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

        val pickMedia =
            registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
                if (uri != null) {
                    loadImageWithRoundedCorners(uri)
                    currentImageUri = uri
                    editViewModel.onImageNewSelected(uri)
                }
            }

        binding.imagePlayer.setOnClickListener {
            pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
        }
    }

    private fun setupPlaylistDetails(playlist: NewPlaylistWithTracks) {
        binding.inputEditNamePlaylist.setText(playlist.name ?: "")
        binding.inputEditDescriptionPlaylist.setText(playlist.description ?: "")

        val imageUrl = playlist.previewUrl
        val cornerRadius = 8
        Glide.with(this)
            .load(imageUrl)
            .error(R.drawable.playplaceholder)
            .transform(RoundedCorners(cornerRadius))
            .into(binding.imagePlayer)
    }

    private fun loadImageWithRoundedCorners(imageSource: Any) {
        val cornerRadius = 8

        val requestOptions = RequestOptions()
            .transform(RoundedCorners(cornerRadius))
            .placeholder(R.drawable.playplaceholder)
            .error(R.drawable.playplaceholder)

        Glide.with(this)
            .load(imageSource ?: R.drawable.playplaceholder)
            .apply(requestOptions)
            .into(binding.imagePlayer)
    }

    override fun onResume() {
        super.onResume()
        requireActivity().findViewById<BottomNavigationView>(R.id.bottomNavigationView).visibility =
            View.GONE
    }
}