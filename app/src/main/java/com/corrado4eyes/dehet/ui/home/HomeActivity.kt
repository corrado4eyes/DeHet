package com.corrado4eyes.dehet.ui.home

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.corrado4eyes.dehet.R
import com.corrado4eyes.dehet.databinding.ActivityMainBinding
import com.corrado4eyes.dehet.di.Modules
import com.corrado4eyes.dehet.models.Filter
import com.corrado4eyes.dehet.ui.favourite.FavouriteActivity
import com.corrado4eyes.dehet.ui.fragments.HistoryListFragment
import com.corrado4eyes.dehet.ui.fragments.ResultFragment
import com.corrado4eyes.dehet.ui.fragments.SearchBarFragment
import com.corrado4eyes.dehet.ui.viewModels.HomeViewModel
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class HomeActivity : AppCompatActivity() {

    companion object {
        private const val TAG = "HomeActivity"
    }

    private val fragmentManager = supportFragmentManager

    private val viewModel: HomeViewModel by lazy {
        ViewModelProvider(this).get(HomeViewModel::class.java)
    }

    private var filter: Filter = Filter.ALL

    private fun attachResultFragment() {
        val transaction = fragmentManager.beginTransaction()
        val fragment = ResultFragment.getInstance()
        transaction.replace(R.id.resultFragment, fragment)
            .addToBackStack(null)
            .commit()
    }
    private fun attachSearchBarFragment() {
        val transaction = fragmentManager.beginTransaction()
        val fragment = SearchBarFragment.getInstance()
        transaction.replace(R.id.searchBarFragment, fragment)
            .addToBackStack(null)
            .commit()
    }

    private fun attachHistoryFragment() {
        val transaction = fragmentManager.beginTransaction()
        val fragment = HistoryListFragment.getInstance()
        transaction.replace(R.id.historyFragment, fragment)
            .addToBackStack(null)
            .commit()
    }

    private fun setupBinding() {
        val binding = DataBindingUtil
            .setContentView<ActivityMainBinding>(this, R.layout.activity_main)
        binding.lifecycleOwner = this
        binding.homeViewModel = viewModel
    }

    private fun initializeDependencyInjection() {
        startKoin {
            androidContext(applicationContext)
            modules(Modules.modules)
        }
    }

    private fun onActionBarButtonTapped(id: Int) {
        MainScope().launch {
            when(id) {
                R.id.noFilter -> {
                    if(filter == Filter.ALL)
                        return@launch
                    viewModel.historyList.value = viewModel.syncUiWithDb()
                    filter = Filter.ALL
                }
                R.id.favouriteFilter -> {
                    if (filter == Filter.FAVOURITE)
                        return@launch
                    // TODO: add method in viewModel that is able to filter the values
                    filter = Filter.FAVOURITE
                }

                R.id.notFavouriteFilter -> {
                    if (filter == Filter.NOT_FAVOURITE)
                        return@launch
                    // TODO: add method in viewModel that is able to filter the values
                    filter = Filter.NOT_FAVOURITE
                }
                R.id.favouriteActionBarButton -> {
                    onFavouriteButtonTapped()
                }
            }
        }
    }

    private fun onFavouriteButtonTapped() {
        val intent = Intent(this, FavouriteActivity::class.java)
        startActivity(intent)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        // Dependency injection
        initializeDependencyInjection()
    }

    override fun onResume() {
        super.onResume()

        // DataBinding
        setupBinding()


        // Attaching fragment
        attachSearchBarFragment()
        attachResultFragment()
        attachHistoryFragment()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_home, menu)
        return true
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        onActionBarButtonTapped(id)

        return true
    }
}
