package com.devgary.contentviewer.feature.history

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.devgary.contentviewer.databinding.FragmentHistoryBinding

class HistoryFragment : Fragment() {
    private val historyViewModel: HistoryViewModel by viewModels()

    private var binding: FragmentHistoryBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        FragmentHistoryBinding.inflate(inflater, container, false).also { 
            binding = it
            return it.root
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}