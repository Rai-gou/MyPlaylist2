package com.example.myplaylist.settings.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.myplaylist.databinding.FragmentSettingsBinding
import org.koin.android.ext.android.inject

const val MY_PREFERENCES = "PREFERENCES"

class SettingsFragment : Fragment()  {

    private var _binding: FragmentSettingsBinding? = null
    private val binding get() = _binding!!
    private val viewModel: SettingsViewModel by inject()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSettingsBinding.inflate(inflater, container, false)
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initializeViews()
        initializeListeners()

    }

    private fun initializeViews() {
        viewModel.loadThemePreference()
        viewModel.themeSettings.observe(viewLifecycleOwner) { themeSettings ->
            binding.switchTheme.isChecked = themeSettings.isNightModeEnabled
        }
    }

    private fun initializeListeners() {
        binding.switchTheme.setOnCheckedChangeListener { _, isChecked ->
            viewModel.saveThemePreference(isChecked)
        }
        binding.buttonShare.setOnClickListener {
            viewModel.shareApp()
        }

        binding.buttonSupport.setOnClickListener {
            viewModel.openSupport()
        }

        binding.buttonAgreement.setOnClickListener {
            viewModel.openTerms()
        }
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}