package com.corrado4eyes.dehet.ui.favourite

import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.corrado4eyes.dehet.R
import com.corrado4eyes.dehet.databinding.ActivityFavouritesBinding
import com.corrado4eyes.dehet.models.HistoryEntry
import com.corrado4eyes.dehet.ui.adapters.FavouritesAdapter
import com.corrado4eyes.dehet.ui.adapters.HistoryEntryEvent
import com.corrado4eyes.dehet.ui.viewModels.FavouritesViewModel
import kotlinx.android.synthetic.main.activity_favourites.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch

class FavouriteActivity: AppCompatActivity(), HistoryEntryEvent, CoroutineScope by MainScope() {

    companion object {
        private const val TAG = "FavouriteActivity"
    }

    private val favouritesViewModel by lazy {
        ViewModelProvider(this).get(FavouritesViewModel::class.java)
    }

    private val favouritesAdapter = FavouritesAdapter(this)

    private fun setupBinding() {
        val binding = DataBindingUtil
            .setContentView<ActivityFavouritesBinding>(this, R.layout.activity_favourites)
        binding.lifecycleOwner = this
        binding.favouritesViewModel = favouritesViewModel
        binding.favouritesList.layoutManager = LinearLayoutManager(this)
        binding.favouritesList.adapter = favouritesAdapter
    }

    private fun initializeValues() {
        MainScope().launch {
            favouritesViewModel.favouritesList.value = favouritesViewModel.syncWithLocalDb()
        }
        Log.d(TAG, favouritesViewModel.favouritesList.value!!.size.toString())
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_favourites)

        // Setup binding
        setupBinding()
    }

    override fun onResume() {
        super.onResume()
        // Initialize values
        initializeValues()
    }

    override fun onFavouriteButtonClicked(entry: HistoryEntry) {
        MainScope().launch {
            favouritesViewModel.onFavouriteButtonTapped(entry)
            favouritesViewModel.favouritesList.value = favouritesViewModel.syncWithLocalDb()
        }
    }

    override fun onDeleteButtonClicked(entry: HistoryEntry) {
        MainScope().launch {
            favouritesViewModel.onDeleteButtonTapped(entry)
            favouritesViewModel.favouritesList.value = favouritesViewModel.syncWithLocalDb()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        cancel()
        finish()
    }

}