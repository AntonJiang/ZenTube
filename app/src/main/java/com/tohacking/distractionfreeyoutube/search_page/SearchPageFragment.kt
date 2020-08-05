package com.tohacking.distractionfreeyoutube.search_page

import android.app.Activity
import android.os.Bundle
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.tohacking.distractionfreeyoutube.application.MainActivity
import com.tohacking.distractionfreeyoutube.application.PlaylistAdapter
import com.tohacking.distractionfreeyoutube.databinding.SearchPageBinding
import timber.log.Timber


class SearchPageFragment : Fragment() {
    lateinit var binding: SearchPageBinding
    lateinit var searchPageViewModel: SearchPageViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val application = requireNotNull(activity).application
        binding = SearchPageBinding.inflate(inflater)
        binding.lifecycleOwner = this
        val viewModelFactory = SearchPageViewModelFactory(application)
        searchPageViewModel =
            ViewModelProvider(this, viewModelFactory).get(SearchPageViewModel::class.java)
        binding.viewModel = searchPageViewModel

        populateRecyclerView()

        // Set up search
        binding.searchSubmitButton.setOnClickListener {
            val query = binding.searchTextView.text.toString()
            if (query != "") {
                searchPageViewModel.requestQuery(query)
            }
        }
        binding.searchTextView.setOnKeyListener(object : View.OnKeyListener {
            override fun onKey(v: View?, keyCode: Int, event: KeyEvent?): Boolean {

                if (event?.action == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER) {
                    //if the enter key was pressed, then hide the keyboard and do whatever needs doing.
                    hideKeyboard(v!!)
                    //do what you need on your enter key press here
                    val query = binding.searchTextView.text.toString()
                    if (query != "")
                        searchPageViewModel.requestQuery(query)
                    return true
                }
                return false
            }
        })
        binding.searchTextView.onFocusChangeListener =
            View.OnFocusChangeListener { v, hasFocus -> if (!hasFocus) hideKeyboard(v!!) }

        return binding.root
    }

    fun hideKeyboard(view: View) {
        val inputMethodManager =
            activity?.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager?
        inputMethodManager!!.hideSoftInputFromWindow(view.windowToken, 0)
    }

    private fun populateRecyclerView() {
        // playlist adapter
        Timber.i("Populating Recycler View")

        val playlistAdapter = PlaylistAdapter(lifecycle, (activity as MainActivity)) {
            Timber.d("requesting next page...")
            searchPageViewModel.requestNextPage()
        }
        binding.searchRecyclerView.adapter = playlistAdapter
        val emptyView = binding.searchEmptyRecyclerView
        Timber.d("emptyView $emptyView")
        binding.searchRecyclerView.setEmptyView(emptyView)

        searchPageViewModel.playlist.observe(viewLifecycleOwner, Observer {
            it?.let {
                Timber.d("Changing adapter data")
                playlistAdapter.data = it
            }
        })
    }
}