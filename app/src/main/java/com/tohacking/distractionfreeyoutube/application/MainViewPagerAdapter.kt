package com.tohacking.distractionfreeyoutube.application

import android.widget.FrameLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.adapter.FragmentViewHolder
import com.tohacking.distractionfreeyoutube.R
import com.tohacking.distractionfreeyoutube.application.enum.PageCount
import com.tohacking.distractionfreeyoutube.history_page.HistoryPageFragment
import com.tohacking.distractionfreeyoutube.playlist_page.PlaylistPageFragment
import com.tohacking.distractionfreeyoutube.profile_page.ProfilePageFragment
import com.tohacking.distractionfreeyoutube.search_page.SearchPageFragment
import com.tohacking.distractionfreeyoutube.subs_page.SubsPageFragment
import timber.log.Timber

class MainViewPagerAdapter(supportFragmentManager: FragmentManager, lifecycle: Lifecycle) :
    FragmentStateAdapter(supportFragmentManager, lifecycle) {
    init {

    }

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        Timber.d("onAttach")
    }

    override fun onDetachedFromRecyclerView(recyclerView: RecyclerView) {
        super.onDetachedFromRecyclerView(recyclerView)
        Timber.d("onDetach")
    }

    override fun onViewDetachedFromWindow(holder: FragmentViewHolder) {
        Timber.d("Detaching from position ${holder.itemId}")
        when (holder.itemId.toInt()) {
            PageCount.PLAYLIST_PAGE_FRAGMENT.pagePosition -> {
            }
            PageCount.SEARCH_PAGE_FRAGMENT.pagePosition -> {
                Timber.d("Detaching search page")
                ((holder.itemView as FrameLayout)
                    .findViewById<RecyclerView>(R.id.searchRecyclerView)
                    .adapter as PlaylistAdapter)
                    .onPauseWithFragment()
            }
            PageCount.SUBS_PAGE_FRAGMENT.pagePosition -> {
            }
            PageCount.PROFILE_PAGE_FRAGMENT.pagePosition -> {
            }
            PageCount.HISTORY_PAGE_FRAGMENT.pagePosition -> {
                Timber.d("Detaching history page")
                ((holder.itemView as FrameLayout)
                    .findViewById<RecyclerView>(R.id.historyRecyclerView)
                    .adapter as PlaylistAdapter)
                    .onPauseWithFragment()
            }
            else -> throw Exception("Cannot select fragment at position ${holder.itemId.toInt()}")
        }

        super.onViewDetachedFromWindow(holder)
    }

    override fun onBindViewHolder(
        holder: FragmentViewHolder,
        position: Int,
        payloads: MutableList<Any>
    ) {
        super.onBindViewHolder(holder, position, payloads)
        Timber.d("onBindViewHolder")
    }

    override fun getItemCount(): Int {
        return 5
    }

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            PageCount.HISTORY_PAGE_FRAGMENT.pagePosition -> HistoryPageFragment()
            PageCount.PLAYLIST_PAGE_FRAGMENT.pagePosition -> PlaylistPageFragment()
            PageCount.SEARCH_PAGE_FRAGMENT.pagePosition -> SearchPageFragment()
            PageCount.SUBS_PAGE_FRAGMENT.pagePosition -> SubsPageFragment()
            PageCount.PROFILE_PAGE_FRAGMENT.pagePosition -> ProfilePageFragment()
            else -> throw Exception("Cannot create fragment page at $position")
        }
    }

}
