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
import timber.log.Timber


class HistoryPageFragment : Fragment() {
    lateinit var binding: HistoryPageBinding
    private lateinit var historyViewModel: HistoryPageViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val application = requireNotNull(activity).application
        binding = HistoryPageBinding.inflate(inflater)
        binding.lifecycleOwner = this
        val viewModelFactory = HistoryPageViewModelFactory(application)

        historyViewModel =
            ViewModelProvider(this, viewModelFactory).get(HistoryPageViewModel::class.java)
        binding.viewModel = historyViewModel

        populateRecyclerView()
        return binding.root
    }

    private fun populateRecyclerView() {
        // playlist adapter
        Timber.i("Populating Recycler View")
        val playlistAdapter = PlaylistAdapter(lifecycle)
        binding.historyRecyclerView.adapter = playlistAdapter

        historyViewModel.playlist.observe(viewLifecycleOwner, Observer {
            it?.let {
                playlistAdapter.data = it
            }
        })


    }


}