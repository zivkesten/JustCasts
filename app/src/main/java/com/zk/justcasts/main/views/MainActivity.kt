package com.zk.justcasts.main.views

import androidx.navigation.findNavController
import androidx.navigation.ui.setupActionBarWithNavController
import com.zk.justcasts.R
import com.zk.justcasts.databinding.ActivityMainBinding
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.FrameLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupActionBarWithNavController
import com.zk.justcasts.shows.viewModel.MyShowsViewModel
import com.zk.justcasts.shows.views.MyShowsFragment


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        supportFragmentManager
            .beginTransaction()
            .replace(binding.mainContainer.id, MyShowsFragment(), MyShowsViewModel::class.java.simpleName)
            .commit()
//        binding.bottomNavigationView.inflateMenu(R.menu.navigation_menu)
//        val navController = findNavController(R.id.nav_host_fragment)
//        NavigationUI.setupWithNavController(
//                binding.bottomNavigationView,
//                navController
//        )
//
//        setupActionBarWithNavController(navController)
//        binding.bottomNavigationView.setOnNavigationItemSelectedListener { item ->
//            when(item.itemId) {
//                R.id.searchFragment -> {
//                    // Respond to navigation item 1 click
//                    true
//                }
//                R.id.showsFragment -> {
//                    // Respond to navigation item 2 click
//                    true
//                }
//
//                R.id.listenNowFragment -> {
//                    // Respond to navigation item 2 click
//                    true
//                }
//                else -> false
//            }
//        }
    }
}