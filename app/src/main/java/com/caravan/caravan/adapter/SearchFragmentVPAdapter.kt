package com.caravan.caravan.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.caravan.caravan.ui.fragment.main.SearchFragmentGuide
import com.caravan.caravan.ui.fragment.main.SearchFragmentTrip

class SearchFragmentVPAdapter(fragmentManager: FragmentManager, lifeCycle: Lifecycle) :
    FragmentStateAdapter(fragmentManager, lifeCycle) {

    override fun getItemCount(): Int {
        return 2
    }

    override fun createFragment(position: Int): Fragment {

        return when (position) {
            0 -> SearchFragmentGuide()
            else -> SearchFragmentTrip()
        }

    }

}