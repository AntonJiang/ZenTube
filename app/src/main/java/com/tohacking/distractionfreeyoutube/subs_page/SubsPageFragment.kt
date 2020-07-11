package com.tohacking.distractionfreeyoutube.subs_page

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.tohacking.distractionfreeyoutube.databinding.SubsPageBinding


class SubsPageFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val application = requireNotNull(activity).application
        val binding = SubsPageBinding.inflate(inflater)
        binding.lifecycleOwner = this
        val viewModelFactory = SubsPageViewModelFactory(application)
        binding.viewModel =
            ViewModelProvider(this, viewModelFactory).get(SubsPageViewModel::class.java)


        return binding.root
    }
}