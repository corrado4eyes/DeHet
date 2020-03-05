package com.corrado4eyes.dehet.ui.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.corrado4eyes.dehet.R
import com.corrado4eyes.dehet.databinding.SegmentedControlFragmentBinding
import com.corrado4eyes.dehet.ui.viewModels.HomeViewModel
import kotlinx.android.synthetic.main.segmented_control_fragment.view.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import org.koin.core.KoinComponent

class SegmentedControlFragment: Fragment(), KoinComponent, CoroutineScope by MainScope() {

    companion object {
        private const val TAG = "SegmentedControlFragment"

        fun getInstance(): SegmentedControlFragment {
            return SegmentedControlFragment()
        }
    }

    private val viewModel by activityViewModels<HomeViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = DataBindingUtil
            .inflate<SegmentedControlFragmentBinding>(inflater, R.layout.segmented_control_fragment,
            container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel
        return binding.root
    }

    private fun onFilterSelected(IsFilterFavourite: Boolean) {
        MainScope().launch {
            if(IsFilterFavourite) {
                viewModel.isFavouriteFilterSelected.value = true
                viewModel.historyList.value =
                    viewModel.reverseList(viewModel.onFilterSelected())
            } else {
                viewModel.isFavouriteFilterSelected.value = false
                viewModel.historyList.value = viewModel.reverseList(viewModel.syncWithLocalDb())
            }
        }

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.segmentedControlFilter.addOnButtonCheckedListener { group, checkedId, isChecked ->
            when(group.checkedButtonId) {
                R.id.filterAll -> onFilterSelected(false)
                R.id.filterFavourites -> onFilterSelected(true)
                else -> Log.d(TAG, "no case")
            }
        }
    }

}