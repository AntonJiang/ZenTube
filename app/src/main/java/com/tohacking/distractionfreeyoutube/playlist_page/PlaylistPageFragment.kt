package com.tohacking.distractionfreeyoutube.playlist_page

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.tohacking.distractionfreeyoutube.databinding.PlaylistPageBinding


class PlaylistPageFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val application = requireNotNull(activity).application
        val binding = PlaylistPageBinding.inflate(inflater)
        binding.lifecycleOwner = this
        val viewModelFactory = PlaylistPageViewModelFactory(application)
        binding.viewModel =
            ViewModelProvider(this, viewModelFactory).get(PlaylistPageViewModel::class.java)


        return binding.root
    }
}