package com.corrado4eyes.dehet.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.BindingAdapter
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.corrado4eyes.dehet.R
import com.corrado4eyes.dehet.databinding.HistoryEntryBinding
import com.corrado4eyes.dehet.models.HistoryEntry
import kotlinx.android.synthetic.main.history_entry.view.*

class FavouritesAdapter(private val entryEvent: HistoryEntryEvent): RecyclerView.Adapter<FavouritesAdapter.EntryHolder>() {

    private var favouritesList: List<HistoryEntry> = listOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EntryHolder {
        val binding = DataBindingUtil.inflate<HistoryEntryBinding>(LayoutInflater.from(parent.context),
            R.layout.history_entry, parent, false)
        return EntryHolder(binding, entryEvent)
    }

    override fun getItemCount(): Int = favouritesList.size

    override fun onBindViewHolder(holder: EntryHolder, position: Int) {
        val entry = favouritesList[position]
        return holder.bind(entry)
    }

    fun updateEntries(newList: List<HistoryEntry>) {
        favouritesList = newList
        notifyDataSetChanged()
    }

    class EntryHolder(private val binding: HistoryEntryBinding,
                      private val entryEvent: HistoryEntryEvent):
        RecyclerView.ViewHolder(binding.root) {

        fun bind(entry: HistoryEntry) {
            setListeners(entry)
            setupUi(entry)
        }

        private fun setListeners(entry: HistoryEntry) {
            itemView.favouriteButton.setOnClickListener {
                entryEvent.onFavouriteButtonClicked(entry)
            }

            itemView.deleteEntryButton.setOnClickListener {
                entryEvent.onDeleteButtonClicked(entry)
            }
        }

        private fun setupUi(entry: HistoryEntry) {
            binding.articleLabel.text = entry.toString()
            binding.favouriteButton.setImage(
                when(entry.isFavourite) {
                    true -> R.drawable.big_bookmark
                    else -> R.drawable.big_empty_bookmark
                }
            )
            binding.deleteEntryButton.setImage(R.drawable.delete_icon)
        }

    }
}

@BindingAdapter("favouritesEntries")
fun RecyclerView.bindFavourites(newList: List<HistoryEntry>) {
    (adapter as? FavouritesAdapter)?.updateEntries(newList)
}