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
import kotlinx.android.synthetic.main.history_entry.view.*

class HistoryAdapter(private val entryEvent: HistoryEntryEvent): RecyclerView.Adapter<HistoryAdapter.EntryHolder>() {

    var historyList: List<HistoryEntry> = listOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EntryHolder {
        val binding = DataBindingUtil.inflate<HistoryEntryBinding>(LayoutInflater.from(parent.context),
            R.layout.history_entry, parent, false)
        return EntryHolder(binding, entryEvent)
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

    class EntryHolder(private val binding: HistoryEntryBinding,
                      private val entryEvent: HistoryEntryEvent):
        RecyclerView.ViewHolder(binding.root) {

        fun bind(entry: HistoryEntry) {
            setListeners()
            setupUi(entry)
        }

        private fun setListeners() {
            itemView.favouriteButton.setOnClickListener {
                entryEvent.onFavouriteButtonClicked(adapterPosition)
            }
        }

        private fun setupUi(entry: HistoryEntry) {
            binding.articleLabel.text = entry.toString()
            binding.favouriteButton.setImage(
                when(entry.isFavourite) {
                    true -> R.drawable.bookmark
                    else -> R.drawable.empty_bookmark
                }
            )
        }

    }
}

interface HistoryEntryEvent {
    fun onFavouriteButtonClicked(position: Int)
}

@BindingAdapter("entriesList")
fun RecyclerView.bindEntries(newList: List<HistoryEntry>) {
    (adapter as? HistoryAdapter)?.updateEntries(newList)
}

@BindingAdapter("bind:entryImg")
fun ImageButton.setImage(@DrawableRes resId: Int) {
    this.load(resId)
}