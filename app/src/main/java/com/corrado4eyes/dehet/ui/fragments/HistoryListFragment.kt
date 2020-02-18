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
import com.corrado4eyes.dehet.ui.viewModels.HomeViewModel

class HistoryListFragment: Fragment() {
    companion object {
        private const val TAG = "HistoryListFragment"

        fun getInstance(): HistoryListFragment {
            return HistoryListFragment()
        }
    }

    private val adapter = HistoryAdapter()
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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d(TAG, "The size is: ${viewModel.historyList.value!!.size}")
    }
}