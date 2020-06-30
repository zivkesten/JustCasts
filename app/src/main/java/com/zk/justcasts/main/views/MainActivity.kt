package com.zk.justcasts.main.views

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.NavigationUI.setupActionBarWithNavController
import com.zk.justcasts.R
import com.zk.justcasts.databinding.ActivityMainBinding
import com.zk.justcasts.main.model.Event
import com.zk.justcasts.main.model.ViewEffect
import com.zk.justcasts.main.viewModel.MainActivityViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private val viewModel: MainActivityViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        binding.bottomNavigationView.inflateMenu(R.menu.navigation_menu)
        val navController = findNavController(R.id.nav_host_fragment)
        NavigationUI.setupWithNavController(
                binding.bottomNavigationView,
                navController
        )
        setupActionBarWithNavController(this, navController)
        navController.addOnDestinationChangedListener { _, destination, _ ->
            Log.d("Zivi", "destination: ${destination.id}")
          viewModel.event(Event.Navigation(destination.id))
        }

        viewModel.obtainViewEffects.observe(this, Observer {
            trigger(it)
        })
    }

    private fun trigger(viewEffect: ViewEffect) {
        when(viewEffect) {
            is ViewEffect.AnimateNavigationViewVisibility -> {
                binding.bottomNavigationView.visibility = viewEffect.visibility
                binding.toolbar.visibility = viewEffect.visibility
            }
        }
    }
}