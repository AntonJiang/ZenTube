package com.tohacking.distractionfreeyoutube.application


import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import com.tohacking.distractionfreeyoutube.R
import com.tohacking.distractionfreeyoutube.databinding.UiBackboneBinding
import com.tohacking.distractionfreeyoutube.repository.data.User
import com.tohacking.distractionfreeyoutube.util.loadUser
import com.tohacking.distractionfreeyoutube.util.saveUser
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import timber.log.Timber

class MainActivity : AppCompatActivity() {
    lateinit var binding: UiBackboneBinding
    lateinit var uiScope: CoroutineScope
    lateinit var uiJob: Job

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Timber.i("Starting Main Activity...")


        // Data binding for ui_backbone.xml
        binding = DataBindingUtil.setContentView(this, R.layout.ui_backbone)

        // Coroutine
        uiJob = Job()
        uiScope = CoroutineScope(Dispatchers.Main + uiJob)

        // Populate top environment menu
        // Dummy Environments
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
        // Default button set to history button
        binding.bottomNavigation.selectedItemId = R.id.history_button

        setUpNavigation()

        uiScope.launch {
            application.loadUser()
        }
    }

    override fun onStop() {
        super.onStop()
        // Only save if isn't anon user
        if (Session.user != User())
            uiScope.launch { application.saveUser() }

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