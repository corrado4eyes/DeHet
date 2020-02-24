package com.corrado4eyes.dehet.ui.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.corrado4eyes.dehet.R
import com.corrado4eyes.dehet.databinding.HistoryFragmentBinding
import com.corrado4eyes.dehet.ui.adapters.HistoryAdapter
import com.corrado4eyes.dehet.ui.adapters.HistoryEntryEvent
import com.corrado4eyes.dehet.ui.viewModels.HomeViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch

class HistoryListFragment: Fragment(), HistoryEntryEvent, CoroutineScope by MainScope() {
    companion object {
        private const val TAG = "HistoryListFragment"

        fun getInstance(): HistoryListFragment {
            return HistoryListFragment()
        }
    }

    private val adapter = HistoryAdapter(this)
    private val viewModel by activityViewModels<HomeViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = DataBindingUtil.inflate<HistoryFragmentBinding>(inflater,
            R.layout.history_fragment,container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel
        binding.historyListView.layoutManager = LinearLayoutManager(activity)
        binding.historyListView.adapter = adapter
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        MainScope().launch {
            viewModel.historyList.value = viewModel.onResumeHistoryState()
        }
    }

    override fun onFavouriteButtonClicked(position: Int) {
        MainScope().launch {
            viewModel.historyList.value = viewModel.onFavouriteButtonClicked(position)
        }
    }
}