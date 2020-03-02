package com.corrado4eyes.dehet.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.corrado4eyes.dehet.R
import com.corrado4eyes.dehet.databinding.HistoryFragmentBinding
import com.corrado4eyes.dehet.models.HistoryEntry
import com.corrado4eyes.dehet.ui.adapters.HistoryAdapter
import com.corrado4eyes.dehet.ui.adapters.HistoryEntryEvent
import com.corrado4eyes.dehet.ui.viewModels.HomeViewModel
import com.corrado4eyes.dehet.util.SwipeToDeleteCallBack
import kotlinx.android.synthetic.main.history_fragment.*
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

    private fun createSwipeHandler(): SwipeToDeleteCallBack {
        return object : SwipeToDeleteCallBack(requireContext()) {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                onEntrySwipedLeft(viewHolder.adapterPosition)
            }
        }
    }

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
        val swipeHandler = createSwipeHandler()
        val itemTouchHelper = ItemTouchHelper(swipeHandler)
        itemTouchHelper.attachToRecyclerView(binding.historyListView)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        savedInstanceState?.run {
            historyListView.layoutManager
                ?.onRestoreInstanceState(getParcelable("HISTORY_LIST_STATE"))
        }
    }

    override fun onResume() {
        super.onResume()
        MainScope().launch {
            viewModel.historyList.value = viewModel.syncWithLocalDb()
        }
    }

    override fun onFavouriteButtonClicked(entry: HistoryEntry) {
        MainScope().launch {
            viewModel.onFavouriteButtonTapped(entry)
            viewModel.historyList.value = viewModel.syncWithLocalDb()
        }
    }

    override fun onEntrySwipedLeft(position: Int) {
        MainScope().launch {
            viewModel.onDeleteButtonTapped(position)
            viewModel.historyList.value = viewModel.syncWithLocalDb()
        }
    }
}