package com.tohacking.distractionfreeyoutube.history_page

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.tohacking.distractionfreeyoutube.application.PlaylistAdapter
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

        val historyViewModel =
            ViewModelProvider(this, viewModelFactory).get(HistoryPageViewModel::class.java)
        binding.viewModel = historyViewModel

        // playlist adapter
        val playlistAdapter = PlaylistAdapter()
        binding.historyRecyclerView.adapter = playlistAdapter

        historyViewModel.playlist.observe(viewLifecycleOwner, Observer {
            it?.let {
                playlistAdapter.data = it
            }
        })

        return binding.root
    }
}