package com.tohacking.distractionfreeyoutube.application

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import com.tohacking.distractionfreeyoutube.R
import com.tohacking.distractionfreeyoutube.databinding.UiBackboneBinding
import timber.log.Timber

class MainActivity : AppCompatActivity() {
    lateinit var binding: UiBackboneBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Timber.i("Started Main Activity")
        binding = DataBindingUtil.setContentView(this, R.layout.ui_backbone)

        // Populate environment menu and items
        val environments =
            arrayOf("Env1", "Env2", "Env3", "Env4")

        val adapter: ArrayAdapter<String> = ArrayAdapter(
            this
            , R.layout.environment_item
            , environments
        )

        val editTextFilledExposedDropdown: AutoCompleteTextView =
            binding.environmentMenuLayout.filledExposedDropdown
        editTextFilledExposedDropdown.setAdapter(adapter)

        // Set up bottom navigation menu
        binding.bottomNavigation.selectedItemId = R.id.history_button
        setUpNavigation()

    }

    // Set up navigation for the main bottom menu
    private fun setUpNavigation() {
        val navController = findNavController(R.id.my_nav_host_fragment)
        binding.bottomNavigation.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.profile_button -> {
                    navController.navigate(R.id.action_global_profilePageFragment)
                    true
                }
                R.id.history_button -> {
                    navController.navigate(R.id.action_global_historyPageFragment)
                    true
                }
                R.id.subscription_button -> {
                    navController.navigate(R.id.action_global_subsPageFragment)
                    true
                }
                R.id.search_button -> {
                    navController.navigate(R.id.searchPageFragment)
                    true
                }
                R.id.playlist_button -> {
                    navController.navigate(R.id.action_global_playlistPageFragment)
                    true
                }
                else -> false
            }
        }
    }
}