package com.tohacking.distractionfreeyoutube.history_page

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.tohacking.distractionfreeyoutube.databinding.HistoryPageBinding


class HistoryPageFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val application = requireNotNull(activity).application
        val binding = HistoryPageBinding.inflate(inflater)
        binding.lifecycleOwner = this
        val viewModelFactory = HistoryPageViewModelFactory(application)
        binding.viewModel = ViewModelProvider(this, viewModelFactory).get(HistoryPageViewModel::class.java)

        return binding.root
    }
}