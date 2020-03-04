package com.corrado4eyes.dehet.ui.fragments

import android.os.Bundle
import android.os.Parcelable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.DividerItemDecoration
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
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.history_fragment.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch

class HistoryListFragment: Fragment(), HistoryEntryEvent, CoroutineScope by MainScope() {

    private val snackbarDuration = 1000

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
                val deletedEntry =
                    viewModel.historyList.value?.get(viewHolder.adapterPosition) ?: HistoryEntry()
                val position = viewHolder.adapterPosition
                onEntrySwipedLeft(position)
                onBuildSnackbar(deletedEntry).show()
            }
        }
    }

    private suspend fun onRestoreItem(deletedEntry: HistoryEntry) {
            viewModel.onAddResultClicked(deletedEntry)
            syncHistory()
    }

    private fun onBuildSnackbar(deletedEntry: HistoryEntry): Snackbar {
        return Snackbar.make(requireView(), "One element deleted", snackbarDuration)
            .setAction("UNDO") {
                MainScope().launch { onRestoreItem(deletedEntry) }
            }
    }

    private fun addDividerToHistoryEntries() {
        historyListView.addItemDecoration(
            DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL)
        )
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
        addDividerToHistoryEntries()
    }

    override fun onResume() {
        super.onResume()
        MainScope().launch {
            syncHistory()
        }
    }

    override fun onFavouriteButtonClicked(entry: HistoryEntry) {
        MainScope().launch {
            viewModel.onFavouriteButtonTapped(entry)
            syncHistory()
        }
    }

    override fun onEntrySwipedLeft(position: Int) {
        MainScope().launch {
            viewModel.onDeleteButtonTapped(position)
            syncHistory()
        }
    }

    private suspend fun syncHistory() {
        val isFavouriteFilterSelected = viewModel.isFavouriteFilterSelected.value!!
        if (isFavouriteFilterSelected) {
            syncByFavourites()
        } else {
            syncByAll()
        }
    }

    private suspend fun syncByAll() {
        viewModel.historyList.value = viewModel.reverseList(viewModel.syncWithLocalDb())
    }

    private suspend fun syncByFavourites() {
        viewModel.historyList.value = viewModel.reverseList(viewModel.onFilterSelected())
    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)
        savedInstanceState?.run {
            val state = getParcelable<Parcelable>("HISTORY_LIST_STATE")
            historyListView.layoutManager
                ?.onRestoreInstanceState(state)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        cancel()
    }
}