package com.example.myplaylist.search.ui

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.myplaylist.databinding.FragmentProblemBinding

class ProblemFragment : Fragment() {

    private var _binding: FragmentProblemBinding? = null
    private val binding get() = _binding!!
    private var listener: FragmentQuery? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentProblemBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.buttonProblem.setOnClickListener {
            val query = arguments?.getString("query") ?: ""
            listener?.onProblemButtonClicked(query)
            parentFragmentManager.popBackStack()
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is FragmentQuery) {
            listener = context
        } else {
            throw RuntimeException("$context must implement FragmentQuery")
        }
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        fun newInstance(query: String): ProblemFragment {
            val fragment = ProblemFragment()
            val args = Bundle().apply {
                putString("query", query)
            }
            fragment.arguments = args
            return fragment
        }
    }
}