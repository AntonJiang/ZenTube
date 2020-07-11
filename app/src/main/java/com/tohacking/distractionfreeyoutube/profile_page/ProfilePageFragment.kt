package com.tohacking.distractionfreeyoutube.profile_page

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.tohacking.distractionfreeyoutube.databinding.ProfilePageBinding
import com.tohacking.distractionfreeyoutube.util.toast


class ProfilePageFragment : Fragment() {
    lateinit var profilePageViewModel: ProfilePageViewModel

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
        }

        requireContext().toast("onCreateView")

        return binding.root
    }


//    private fun updateUserProfile() {
//        val prefs = activity?.getSharedPreferences(EnvironmentVariable.USER_INFO, Context.MODE_PRIVATE)
//        requireContext().toast(prefs?.all.toString())
//        profilePageViewModel.updateDisplayName(prefs?.getString(EnvironmentVariable.PREF_KEY_USERNAME, "Anonymous")!!)
//        profilePageViewModel.updateEmail(prefs.getString(EnvironmentVariable.PREF_KEY_EMAIL, "No Email")!!)
//        profilePageViewModel.updatePhotoUrl(prefs.getString(EnvironmentVariable.PREF_KEY_PHOTOURL, "No Photo")!!)
//    }
}