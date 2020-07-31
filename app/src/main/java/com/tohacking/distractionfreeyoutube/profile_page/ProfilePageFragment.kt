package com.tohacking.distractionfreeyoutube.profile_page

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.tohacking.distractionfreeyoutube.application.LoginActivity
import com.tohacking.distractionfreeyoutube.databinding.ProfilePageBinding
import com.tohacking.distractionfreeyoutube.util.toast


class ProfilePageFragment : Fragment() {
    private lateinit var profilePageViewModel: ProfilePageViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val application = requireNotNull(activity).application

        val binding = ProfilePageBinding.inflate(inflater)
        binding.lifecycleOwner = this
        val viewModelFactory = ProfilePageViewModelFactory(application)

        profilePageViewModel =
            ViewModelProvider(this, viewModelFactory).get(ProfilePageViewModel::class.java)

        binding.viewModel = profilePageViewModel

        binding.logOutButton.setOnClickListener {
            profilePageViewModel.logout()
        }

        profilePageViewModel.loggedOff.observe(viewLifecycleOwner, Observer {
            if (it) {
                val startActivityIntent = Intent(activity, LoginActivity::class.java)
                startActivityIntent.flags =
                    Intent.FLAG_ACTIVITY_CLEAR_TASK and Intent.FLAG_ACTIVITY_NEW_TASK and Intent.FLAG_ACTIVITY_CLEAR_TOP
                startActivity(startActivityIntent)
            }
        })

        requireContext().toast("onCreateViewsss")

        return binding.root
    }
}