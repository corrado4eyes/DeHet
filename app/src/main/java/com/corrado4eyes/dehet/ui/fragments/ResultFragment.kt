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
import com.corrado4eyes.dehet.ui.viewModels.HomeViewModel
import kotlinx.android.synthetic.main.result_fragment.view.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch

class ResultFragment: Fragment(), CoroutineScope by MainScope() {
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

    private fun onAddResultClicked() {
        MainScope().launch {
            viewModel.historyList.value = viewModel.onAddResultClicked()
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.addResultButton.setOnClickListener {
            onAddResultClicked()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        cancel()
    }
}