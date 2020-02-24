package com.corrado4eyes.dehet.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.annotation.DrawableRes
import androidx.databinding.BindingAdapter
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import coil.api.load
import com.corrado4eyes.dehet.R
import com.corrado4eyes.dehet.databinding.HistoryEntryBinding
import com.corrado4eyes.dehet.models.HistoryEntry
import com.corrado4eyes.dehet.ui.viewModels.HistoryEntryViewModel
import kotlinx.android.synthetic.main.history_entry.view.*

class HistoryAdapter: RecyclerView.Adapter<HistoryAdapter.EntryHolder>() {

    var historyList: List<HistoryEntry> = listOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EntryHolder {
        val binding = DataBindingUtil.inflate<HistoryEntryBinding>(LayoutInflater.from(parent.context),
            R.layout.history_entry, parent, false)
        return EntryHolder(binding)
    }

    override fun getItemCount(): Int = historyList.size

    override fun onBindViewHolder(holder: EntryHolder, position: Int) {
        val entry = historyList[position]
        return holder.bind(entry)
    }

    fun updateEntries(newList: List<HistoryEntry>) {
        historyList = newList
        notifyDataSetChanged()
    }

    class EntryHolder(private val binding: HistoryEntryBinding):
        RecyclerView.ViewHolder(binding.root) {

        fun bind(entry: HistoryEntry) {
            val viewModel = HistoryEntryViewModel(entry)
            binding.viewModel = viewModel
            itemView.favouriteButton.setOnClickListener {
                viewModel.onToggleFavouriteButton()
            }
        }

    }
}

@BindingAdapter("entriesList")
fun RecyclerView.bindEntries(newList: List<HistoryEntry>) {
    (adapter as? HistoryAdapter)?.updateEntries(newList)
}

@BindingAdapter("bind:entryImg")
fun ImageButton.setImage(@DrawableRes resId: Int) {
    this.load(resId)
}