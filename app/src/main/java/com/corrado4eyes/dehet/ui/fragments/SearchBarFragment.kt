package com.corrado4eyes.dehet.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.corrado4eyes.dehet.R
import com.corrado4eyes.dehet.databinding.SearchBarFragmentBinding
import com.corrado4eyes.dehet.ui.viewModels.HomeViewModel
import kotlinx.android.synthetic.main.search_bar_fragment.view.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch

class SearchBarFragment: Fragment(), CoroutineScope by MainScope() {

    companion object {
        private const val TAG = "SearchBarFragment"
        fun getInstance(): SearchBarFragment {
            return SearchBarFragment()
        }
    }

    private val viewModel by activityViewModels<HomeViewModel>()

    private fun onSearchButtonClicked() {
        MainScope().launch {
            viewModel.resultLabel.value = viewModel.onSearchButtonClicked()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = DataBindingUtil
            .inflate<SearchBarFragmentBinding>(inflater,
                R.layout.search_bar_fragment, container, false)
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.searchArticleBtn.setOnClickListener {
            onSearchButtonClicked()
        }
    }
}