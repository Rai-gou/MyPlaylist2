package com.example.myplaylist.library.ui

import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.activity.addCallback
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.example.myplaylist.R
import com.example.myplaylist.databinding.FragmentNewPlaylistBinding
import com.example.myplaylist.library.domain.PlaylistInteractor
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import org.koin.android.ext.android.inject

class NewPlaylistFragment : Fragment() {
    private var _binding: FragmentNewPlaylistBinding? = null
    private val binding get() = _binding!!

    private val playlistInteractor: PlaylistInteractor by inject()

    private val viewModel: NewPlaylistViewModel by viewModels {
        NewPlaylistViewModelFactory(playlistInteractor)
    }

    private val playlistNameChanged = Observer<Boolean> { playlistNameChanged ->
        binding.buttonNewPlaylist.isEnabled = playlistNameChanged
    }

    private val nameObserver = Observer<String> { name ->
        if (binding.inputEditNamePlaylist.text.toString() != name) {
            binding.inputEditNamePlaylist.setText(name)
        }
    }

    private val descriptionObserver = Observer<String> { description ->
        if (binding.inputEditDescriptionPlaylist.text.toString() != description) {
            binding.inputEditDescriptionPlaylist.setText(description)
        }
    }

    private val imageObserver = Observer<Uri?> { uri ->
        uri?.let { binding.imagePlayer.setImageURI(it) }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentNewPlaylistBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.backPlayerPlaylist.setOnClickListener {
            handleBackButton()
        }
        binding.buttonNewPlaylist.isEnabled = false

        binding.inputEditNamePlaylist.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                viewModel.onPlaylistNameChanged(s.toString())
                val textInputLayout = binding.textViewLabel
                if (!s.isNullOrEmpty()) {
                    binding.textViewLabel.visibility = View.VISIBLE
                    binding.inputEditNamePlaylist.setBackgroundResource(R.drawable.name_focuse)
                } else {
                    textInputLayout.setTextColor(ContextCompat.getColor(requireContext(), R.color.gray))
                    binding.textViewLabel.visibility = View.GONE
                    binding.inputEditNamePlaylist.setBackgroundResource(R.drawable.name)
                }
            }

            override fun afterTextChanged(s: Editable?) {}
        })

        binding.inputEditDescriptionPlaylist.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                viewModel.onPlaylistDescriptionChanged(s.toString())
                val textInputLayout = binding.textViewLabel
                if (!s.isNullOrEmpty()) {
                    binding.textViewDescription.visibility = View.VISIBLE
                    binding.inputEditDescriptionPlaylist.setBackgroundResource(R.drawable.name_focuse)
                } else {
                    textInputLayout.setTextColor(ContextCompat.getColor(requireContext(), R.color.gray))
                    binding.textViewDescription.visibility = View.GONE
                    binding.inputEditDescriptionPlaylist.setBackgroundResource(R.drawable.name)
                }
            }

            override fun afterTextChanged(s: Editable?) {}
        })

        val pickMedia = registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
            if (uri != null) {
                binding.imagePlayer.setImageURI(uri)
                viewModel.onImageSelected(uri)
            } else {
                Log.d("PhotoPicker", "No media selected")
            }
        }

        binding.imagePlayer.setOnClickListener {
            pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
        }

        binding.buttonNewPlaylist.setOnClickListener {
            viewModel.createPlaylist()
            showToast(binding.inputEditNamePlaylist.text.toString())
            parentFragmentManager.setFragmentResult("newPlaylistRequestKey", Bundle().apply {
                putBoolean("playlistCreated", true)
            })
            parentFragmentManager.popBackStackImmediate()
        }

        viewModel.playlistNameChanged.observe(viewLifecycleOwner, playlistNameChanged)
        viewModel.playlistName.observe(viewLifecycleOwner, nameObserver)
        viewModel.playlistDescription.observe(viewLifecycleOwner, descriptionObserver)
        viewModel.playlistImageUri.observe(viewLifecycleOwner, imageObserver)
    }

    private fun handleBackButton() {
        val isDataChanged = viewModel.isDataChanged.value ?: false
        if (isDataChanged) {
            showExitConfirmationDialog()
        } else {
            parentFragmentManager.popBackStackImmediate()
        }
    }

    private fun showExitConfirmationDialog() {
        context?.let {
            MaterialAlertDialogBuilder(it)
                .setTitle("Завершить создание плейлиста?")
                .setMessage("Все несохраненные данные будут потеряны")
                .setNeutralButton("Отмена") { dialog, which -> }
                .setPositiveButton("Завершить") { dialog, which ->
                    parentFragmentManager.popBackStackImmediate()
                }
                .show()
        }
    }

    private fun showToast(name: String) {
        val inflater = layoutInflater
        val layout: View = inflater.inflate(R.layout.fragment_toast, null)

        val text: TextView = layout.findViewById(R.id.toastText)
        text.text = getString(R.string.playlist_add, name)

        with(Toast(requireContext())) {
            duration = Toast.LENGTH_SHORT
            view = layout
            show()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        viewModel.playlistNameChanged.removeObserver(playlistNameChanged)
        viewModel.playlistName.removeObserver(nameObserver)
        viewModel.playlistDescription.removeObserver(descriptionObserver)
        viewModel.playlistImageUri.removeObserver(imageObserver)
        _binding = null
    }

    override fun onResume() {
        super.onResume()
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            handleBackButton()
        }
    }
}