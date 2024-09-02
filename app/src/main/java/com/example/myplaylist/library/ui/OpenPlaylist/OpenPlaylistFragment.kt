package com.example.myplaylist.library.ui.OpenPlaylist

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.addCallback
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.example.myplaylist.R
import com.example.myplaylist.databinding.FragmentOpenPlaylistBinding
import com.example.myplaylist.library.data.NewPlaylist
import com.example.myplaylist.library.data.NewPlaylistWithTracks
import com.example.myplaylist.library.ui.Playlist.PlaylistFragmentViewModel
import com.example.myplaylist.main.ui.RootActivity
import com.example.myplaylist.main.ui.TrackWordFormUtil
import com.example.myplaylist.main.utils.toMinutes
import com.example.myplaylist.player.model.Track
import com.example.myplaylist.player.ui.MediaPlayActivity
import com.example.myplaylist.search.ui.START_MEDIA_PUT_TRACK
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import org.koin.android.ext.android.inject

class OpenPlaylistFragment : Fragment() {

    private var _binding: FragmentOpenPlaylistBinding? = null
    private val binding get() = _binding!!

    private val openPlaylistViewModel: OpenPlaylistViewModel by inject()

    private val playlistFragmentViewModel: PlaylistFragmentViewModel by inject()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentOpenPlaylistBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            findNavController().navigateUp()
        }
        binding.backOpenPlaylist.setOnClickListener {
            findNavController().navigateUp()
        }
        (requireActivity() as? RootActivity)?.setBottomNavigationVisibility(false)

        val playlistId = arguments?.getString("playlistId")
        playlistId?.let {
            openPlaylistViewModel.loadPlaylist(it)
        } ?: run {
            Toast.makeText(context, "Playlist ID is missing", Toast.LENGTH_SHORT).show()
        }

        openPlaylistViewModel.playlistWithTracks.observe(viewLifecycleOwner) { playlistWithTracks ->
            playlistWithTracks?.let {
                setupPlaylistDetails(it)
                setupPlaylistDetailsInformation(it)
            }
        }

        openPlaylistViewModel.playlistImageUri.observe(viewLifecycleOwner) { uri ->
            loadImageWithRoundedCorners(uri)
        }

        binding.imageShare.setOnClickListener {
            sharePlaylist()
        }

        val bottomSheetContainerEditMenu = binding.editMenu

        val bottomSheetBehaviorEditMenu =
            BottomSheetBehavior.from(bottomSheetContainerEditMenu).apply {
                state = BottomSheetBehavior.STATE_HIDDEN
            }
        binding.optionShare.setOnClickListener {
            sharePlaylist()
            bottomSheetBehaviorEditMenu.state = BottomSheetBehavior.STATE_HIDDEN
        }

        binding.optionEdit.setOnClickListener {
            openPlaylistViewModel.playlistNewPlaylist.value?.let { playlist ->
                editPlaylist(playlist)
            }
            bottomSheetBehaviorEditMenu.state = BottomSheetBehavior.STATE_HIDDEN
        }

        binding.optionDelete.setOnClickListener {
            openPlaylistViewModel.playlistNewPlaylist.value?.let { playlist ->
                confirmDeletePlaylist(playlist)
            }
            bottomSheetBehaviorEditMenu.state = BottomSheetBehavior.STATE_HIDDEN
        }

        bottomSheetBehaviorEditMenu.addBottomSheetCallback(object :
            BottomSheetBehavior.BottomSheetCallback() {
            override fun onStateChanged(bottomSheet: View, newState: Int) {
                when (newState) {
                    BottomSheetBehavior.STATE_HIDDEN -> {
                        binding.overlay.visibility = View.GONE
                        binding.listOpenPlaylist.visibility = View.VISIBLE
                    }

                    else -> {
                        binding.overlay.visibility = View.VISIBLE
                    }
                }
            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) {
                Log.d("bottomSheetBehavior", "onSlide")
            }
        })

        binding.imageMenu.setOnClickListener {
            bottomSheetBehaviorEditMenu.state = BottomSheetBehavior.STATE_COLLAPSED
            binding.listOpenPlaylist.visibility = View.GONE
        }
    }

    private fun setupPlaylistDetails(playlist: NewPlaylistWithTracks) {
        binding.nameOpenPlaylist.text = playlist.name
        binding.descriptionOpenPlaylist.text = playlist.description

        val durationSum = playlist.trackList
            .mapNotNull { it.trackTimeMillis }
            .sum()

        val minutes = durationSum.toMinutes()

        binding.timeAllTrack.text = "$minutes ${getMinuteWordForm(minutes)}"

        binding.trackCount.text = "${playlist.trackCount} ${TrackWordFormUtil.getTrackWordForm(playlist.trackCount)}"

        val trackAdapter = OpenPlaylistAdapter(
            playlist.trackList,
            onTrackClick = { track -> openAudioPlayer(track) },
            onTrackLongClick = { track -> showDeleteTrackDialog(track, playlist.id) }
        )
        binding.recyclerViewOpenPlaylist.adapter = trackAdapter
        binding.recyclerViewOpenPlaylist.layoutManager = LinearLayoutManager(context)
    }

    private fun setupPlaylistDetailsInformation(playlist: NewPlaylistWithTracks) {
        binding.titleTextEditPlaylist.text = playlist.name
        binding.listMediaEditPlaylist.text =
            "${playlist.trackCount} ${TrackWordFormUtil.getTrackWordForm(playlist.trackCount)}"

        val imageUrl = playlist.previewUrl
        val cornerRadius = 2
        Glide.with(this)
            .load(imageUrl)
            .error(R.drawable.placeholder)
            .transform(RoundedCorners(cornerRadius))
            .into(binding.imageEditPlaylist)
    }

    private fun getMinuteWordForm(minutes: Long): String {
        val lastDigit = minutes % 10
        val lastTwoDigits = minutes % 100
        return when {
            lastTwoDigits in 11..19 -> getString(R.string.minute_eleven)
            lastDigit == 1L -> getString(R.string.minute_one)
            lastDigit in 2..4 -> getString(R.string.minute_two)
            else -> getString(R.string.minute_eleven)
        }
    }

    private fun loadImageWithRoundedCorners(uri: Uri?) {

        val cornerRadius = 8
        val requestOptions = RequestOptions()
            .transform(RoundedCorners(cornerRadius))
            .placeholder(R.drawable.playplaceholder)
            .error(R.drawable.playplaceholder)

        Glide.with(this)
            .load(uri ?: R.drawable.playplaceholder)
            .apply(requestOptions)
            .into(binding.imageOpenPlaylist)
    }

    private fun openAudioPlayer(track: Track) {
        val bundle = Bundle().apply {
            putString("trackId", track.trackId)
        }
        startMediaPlayerActivity(track)
    }

    private fun showDeleteTrackDialog(track: Track, playlistId: String) {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle(getString(R.string.delete_track))
            .setMessage(getString(R.string.delete_track_true))
            .setNegativeButton(getString(R.string.cansel), null)
            .setPositiveButton(getString(R.string.delete)) { _, _ ->
                openPlaylistViewModel.deleteTrackFromPlaylist(track.trackId, playlistId)
                playlistFragmentViewModel.refreshPlaylists()
            }
            .show()
    }

    private fun startMediaPlayerActivity(track: Track) {
        val intent = Intent(requireContext(), MediaPlayActivity::class.java)
        intent.putExtra(START_MEDIA_PUT_TRACK, track)
        startActivity(intent)
    }

    private fun confirmDeletePlaylist(playlist: NewPlaylist) {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle(getString(R.string.delete_playlist, playlist.name))
            .setNegativeButton(getString(R.string.no), null)
            .setPositiveButton(getString(R.string.yes)) { _, _ ->
                deletePlaylist(playlist.id)
            }
            .show()
    }

    private fun editPlaylist(playlist: NewPlaylist) {
        val bundle = Bundle().apply {
            putString("playlistId", playlist.id)
        }
        findNavController().navigate(
            R.id.action_openPlaylistFragment_to_editPlaylistFragment,
            bundle
        )
    }

    private fun deletePlaylist(playlistId: String) {
        openPlaylistViewModel.deletePlaylist(playlistId)
        parentFragmentManager.popBackStack()
    }

    private fun sharePlaylist() {
        openPlaylistViewModel.playlistWithTracks.value?.let { playlist ->
            if (playlist.trackCount > 0) {
                val shareIntent = Intent(Intent.ACTION_SEND).apply {
                    type = "text/plain"
                    putExtra(Intent.EXTRA_TEXT, formatPlaylistForSharing(playlist))
                }
                startActivity(Intent.createChooser(shareIntent, getString(R.string.share_playlist)))
            } else {
                Toast.makeText(
                    requireContext(),
                    getString(R.string.no_share_playlist),
                    Toast.LENGTH_LONG
                ).show()
            }
        } ?: run {
            Toast.makeText(requireContext(), getString(R.string.error_share_playlist), Toast.LENGTH_SHORT)
                .show()
        }
    }

    private fun formatPlaylistForSharing(playlist: NewPlaylistWithTracks): String {
        val trackListText = playlist.trackList.mapIndexed { index, track ->
            "${index + 1}. ${track.artistName} - ${track.trackName} (${
                track.trackTimeMillis?.div(
                    1000
                )
            } s)"
        }.joinToString("\n")

        return """
            ${playlist.name}
            
            ${playlist.description}

            [${playlist.trackCount}] getString(R.string.track)
            
            $trackListText
        """.trimIndent()
    }

    override fun onResume() {
        super.onResume()
        requireActivity().findViewById<BottomNavigationView>(R.id.bottomNavigationView).visibility =
            View.GONE
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}