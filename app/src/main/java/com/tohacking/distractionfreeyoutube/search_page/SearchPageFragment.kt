package com.tohacking.distractionfreeyoutube.search_page

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.tohacking.distractionfreeyoutube.databinding.SearchPageBinding


class SearchPageFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val application = requireNotNull(activity).application
        val binding = SearchPageBinding.inflate(inflater)
        binding.lifecycleOwner = this
        val viewModelFactory = SearchPageViewModelFactory(application)
        binding.viewModel =
            ViewModelProvider(this, viewModelFactory).get(SearchPageViewModel::class.java)


        return binding.root
    }
}