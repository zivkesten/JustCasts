package com.zk.justcasts.main.views

import com.zk.justcasts.databinding.ActivityMainBinding
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.NavigationUI.setupActionBarWithNavController
import com.zk.justcasts.R


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
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
    }
}