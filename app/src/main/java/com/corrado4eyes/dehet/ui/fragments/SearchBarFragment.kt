package com.corrado4eyes.dehet.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.corrado4eyes.dehet.R
import com.corrado4eyes.dehet.databinding.SearchBarFragmentBinding
import com.corrado4eyes.dehet.models.Filter
import com.corrado4eyes.dehet.models.HistoryEntry
import com.corrado4eyes.dehet.ui.viewModels.HomeViewModel
import com.corrado4eyes.dehet.util.NetworkUtil
import kotlinx.android.synthetic.main.search_bar_fragment.view.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.cancel
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
        if (!isConnectionAvailable()) {
            Toast.makeText(requireContext(),
                "You need to be connected to search new words!",
                Toast.LENGTH_LONG).show()
            return
        }

        MainScope().launch {
            // Fetching the word
            onFetchWord()
            // Adding result to history
            onAddResult()
        }

    }

    private fun isConnectionAvailable(): Boolean {
        if(!NetworkUtil.isDeviceConnected()) {
            return false
        }
        return true
    }

    private suspend fun onFetchWord() {
        val givenText = viewModel.editTextValue.value ?: ""
        if(givenText != "") {
            viewModel.resultHistoryEntry.value = viewModel
                .onSearchButtonClicked(givenText)
        } else {
            Toast.makeText(activity, "The text box is empty!", Toast.LENGTH_SHORT).show()
        }
    }

    private suspend fun onSyncHistory(): List<HistoryEntry> {
        return when(viewModel.isFavouriteFilterSelected.value!!) {
            Filter.ALL -> viewModel.reverseList(viewModel.syncWithLocalDb())
            Filter.FAVOURITE -> viewModel.reverseList(viewModel.onFilterSelected())
        }
    }

    private suspend fun onAddResult() {
        val newEntry = viewModel.resultHistoryEntry.value
            if(newEntry != null) {
                viewModel.onAddResultClicked(newEntry)
                viewModel.historyList.value = onSyncHistory()
            } else {
                Toast.makeText(context, "The result field is empty", Toast.LENGTH_SHORT).show()
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

    override fun onDestroy() {
        super.onDestroy()
        cancel()
    }
}