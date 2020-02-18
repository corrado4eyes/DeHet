package com.corrado4eyes.dehet.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.corrado4eyes.dehet.databinding.HistoryEntryBinding
import com.corrado4eyes.dehet.models.HistoryEntry
import com.corrado4eyes.dehet.ui.viewModels.HistoryEntryViewModel

class HistoryAdapter: RecyclerView.Adapter<HistoryAdapter.EntryHolder>() {

    var historyList: List<HistoryEntry> = listOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EntryHolder {
        val view = HistoryEntryBinding.inflate(LayoutInflater.from(parent.context),
            parent, false)
        return EntryHolder(view)
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

    class EntryHolder(private val view: HistoryEntryBinding): RecyclerView.ViewHolder(view.root) {
        fun bind(entry: HistoryEntry) {
            view.viewModel = HistoryEntryViewModel(entry)
        }
    }
}

@BindingAdapter("entriesList")
fun RecyclerView.bindEntries(newList: List<HistoryEntry>) {
    (adapter as? HistoryAdapter)?.updateEntries(newList)
}