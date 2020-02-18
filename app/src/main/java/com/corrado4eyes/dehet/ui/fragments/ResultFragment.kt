package com.corrado4eyes.dehet.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.corrado4eyes.dehet.R
import com.corrado4eyes.dehet.databinding.ResultFragmentBinding
import com.corrado4eyes.dehet.databinding.SearchBarFragmentBinding
import com.corrado4eyes.dehet.ui.viewModels.HomeViewModel

class ResultFragment: Fragment() {
    companion object {
        fun getInstance(): ResultFragment {
            return ResultFragment()
        }
    }

    private val viewModel by activityViewModels<HomeViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = DataBindingUtil
            .inflate<ResultFragmentBinding>(inflater,
                R.layout.result_fragment, container, false)
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }

}