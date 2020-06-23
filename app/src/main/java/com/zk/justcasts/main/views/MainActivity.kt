package com.zk.justcasts.main.views

import android.opengl.Visibility
import com.zk.justcasts.databinding.ActivityMainBinding
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.get
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.NavigationUI.setupActionBarWithNavController
import com.zk.justcasts.R
import com.zk.justcasts.main.model.Event
import com.zk.justcasts.main.model.ViewEffect
import com.zk.justcasts.main.viewModel.MainActivityViewModel
import kotlinx.android.synthetic.main.activity_main.*
import org.koin.androidx.viewmodel.ext.android.viewModel


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private val viewModel: MainActivityViewModel by viewModel<MainActivityViewModel>()

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
            is ViewEffect.AnimateNavigationViewVisibility ->
                if (viewEffect.visibility == View.GONE) {
                    hideBottomNavigation()
                } else {
                    showBottomNavigation()
                }
        }
    }


    private fun hideBottomNavigation() {
        // bottom_navigation is BottomNavigationView
        with(binding.bottomNavigationView) {
            if (visibility == View.VISIBLE && alpha == 1f) {
                animate()
                    .alpha(0f)
                    .withEndAction { visibility = View.GONE }
                    .duration = 300
            }
        }
    }

    private fun showBottomNavigation() {
        // bottom_navigation is BottomNavigationView
        with(binding.bottomNavigationView) {
            visibility = View.VISIBLE
            animate()
                .alpha(1f)
                .duration = 300
        }
    }
}