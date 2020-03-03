package com.corrado4eyes.dehet.ui.home

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.corrado4eyes.dehet.R
import com.corrado4eyes.dehet.databinding.ActivityMainBinding
import com.corrado4eyes.dehet.di.Modules
import com.corrado4eyes.dehet.ui.fragments.HistoryListFragment
import com.corrado4eyes.dehet.ui.fragments.ResultFragment
import com.corrado4eyes.dehet.ui.fragments.SearchBarFragment
import com.corrado4eyes.dehet.ui.fragments.SegmentedControlFragment
import com.corrado4eyes.dehet.ui.viewModels.HomeViewModel
import com.corrado4eyes.dehet.util.NetworkUtil
import kotlinx.android.synthetic.main.history_fragment.*
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.GlobalContext
import org.koin.core.context.startKoin

class HomeActivity : AppCompatActivity() {

    companion object {
        private const val TAG = "HomeActivity"
    }

    private val fragmentManager = supportFragmentManager

    private val viewModel: HomeViewModel by lazy {
        ViewModelProvider(this).get(HomeViewModel::class.java)
    }

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

    private fun attachSegmentedControlFragment() {
        val transaction = fragmentManager.beginTransaction()
        val fragment = SegmentedControlFragment.getInstance()
        transaction.replace(R.id.segmentedControlFragment, fragment)
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        // Dependency injection
        if(GlobalContext.getOrNull() == null) {
            initializeDependencyInjection()
        }
    }

    override fun onResume() {
        super.onResume()

        // DataBinding
        setupBinding()

        // Monitoring Internet connection
        NetworkUtil.registerNetworkCallback(this)

        // Attaching fragment
        attachSearchBarFragment()
        attachResultFragment()
        attachHistoryFragment()
        attachSegmentedControlFragment()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        val historyListViewState = historyListView.layoutManager?.onSaveInstanceState()
        outState.putParcelable("HISTORY_LIST_STATE", historyListViewState)
        super.onSaveInstanceState(outState)

    }

    override fun onStop() {
        NetworkUtil.unregisterNetworkCallback(this)
        super.onStop()
    }
}
