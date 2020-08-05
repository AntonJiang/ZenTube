package com.tohacking.distractionfreeyoutube.history_page

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.tohacking.distractionfreeyoutube.R
import com.tohacking.distractionfreeyoutube.application.MainActivity
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

        val playlistAdapter = PlaylistAdapter(lifecycle, (activity as MainActivity)) {
        }
        binding.historyRecyclerView.adapter = playlistAdapter
        val emptyView = binding.historyEmptyRecyclerView
        Timber.d("emptyView $emptyView")
        binding.historyRecyclerView.setEmptyView(emptyView)

        historyViewModel.playlist.observe(viewLifecycleOwner, Observer {
            it?.let {
                Timber.d("Observing videos ${it.size}")
                playlistAdapter.data = it
            }
        })
    }


}
