package com.example.myplaylist.main.ui

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.example.myplaylist.R
import com.example.myplaylist.databinding.ActivityRootBinding
import com.example.myplaylist.player.data.db.AppDatabase
import com.example.myplaylist.search.ui.FragmentQuery
import com.example.myplaylist.search.ui.SearchFragment
import com.example.myplaylist.settings.domain.AppSettings
import com.google.android.material.bottomnavigation.BottomNavigationView
import org.koin.android.ext.android.inject

class RootActivity : AppCompatActivity(), FragmentQuery {
    private lateinit var db: AppDatabase
    private lateinit var binding: ActivityRootBinding
    private val appSettings: AppSettings by inject()
    private var searchFragment: SearchFragment? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRootBinding.inflate(layoutInflater)
        setContentView(binding.root)
        //deleteDatabase("database.db")
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.container_view) as NavHostFragment
        val navController = navHostFragment.navController

        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottomNavigationView)
        bottomNavigationView.setupWithNavController(navController)

        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.newPlaylistFragment -> bottomNavigationView.visibility = View.GONE
                else -> bottomNavigationView.visibility = View.VISIBLE
            }
        }

        val savedThemeMode = appSettings.themeMode
        updateTheme(savedThemeMode == AppCompatDelegate.MODE_NIGHT_YES)
    }

    private fun updateTheme(isNightModeEnabled: Boolean) {
        if (isNightModeEnabled) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        }
    }


    override fun onProblemButtonClicked(query: String) {
        searchFragment?.performSearchWithCurrentText()
    }

    fun registerSearchFragment(fragment: SearchFragment) {
        searchFragment = fragment
    }

    override fun onResume() {
        super.onResume()
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.container_view) as NavHostFragment
        val navController = navHostFragment.navController

        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottomNavigationView)
        val currentDestination = navController.currentDestination?.id

        when (currentDestination) {
            R.id.newPlaylistFragment -> bottomNavigationView.visibility = View.GONE
            else -> bottomNavigationView.visibility = View.VISIBLE
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        searchFragment = null
    }

}