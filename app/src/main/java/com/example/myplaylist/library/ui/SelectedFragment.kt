package com.example.myplaylist.library.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.myplaylist.databinding.FragmentSelectedBinding
import org.koin.android.ext.android.inject

class SelectedFragment : Fragment() {

    private lateinit var binding: FragmentSelectedBinding
    private val selectedFragmentViewModel: SelectedFragmentViewModel by inject()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSelectedBinding.inflate(inflater, container, false)
        return binding.root
    }
}