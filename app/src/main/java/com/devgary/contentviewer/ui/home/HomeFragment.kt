package com.devgary.contentviewer.ui.home

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.devgary.contentcore.util.TAG
import com.devgary.contentviewer.databinding.FragmentHomeBinding
import com.devgary.contentviewer.util.hideKeyboard
import com.devgary.contentviewer.util.onAction

class HomeFragment : Fragment() {
    private val homeViewModel: HomeViewModel by lazy {
        ViewModelProvider(this).get(HomeViewModel::class.java)
    }
    
    private var binding: FragmentHomeBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        FragmentHomeBinding.inflate(inflater, container, false).also { 
            binding = it
            return it.root
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
        initViewModel()
    }

    private fun initViews() {
        binding?.apply {
            urlEditText.onAction(EditorInfo.IME_ACTION_GO) {
                hideKeyboard()
                homeViewModel.loadContent(urlEditText.text.toString())
            }
        } ?: run {
            Log.e(TAG, "Trying to access ViewBinding that is null")
        }
    }

    private fun initViewModel() {
        homeViewModel.content.observe(viewLifecycleOwner) {
            binding?.contentView?.showContent(it)
        }
        
        homeViewModel.error.observe(viewLifecycleOwner) {
            showErrorMessage(it)
        }
    }

    private fun showErrorMessage(message: String) {
        Toast.makeText(
            /* context = */ context,
            /* text = */ "Error: $message",
            /* duration = */ Toast.LENGTH_LONG
        ).show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}