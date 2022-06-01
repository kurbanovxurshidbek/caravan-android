package com.caravan.caravan.ui.fragment.details

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.caravan.caravan.R
import com.caravan.caravan.databinding.FragmentGuideTripsBinding
import com.caravan.caravan.ui.fragment.BaseFragment

class GuideTrips : BaseFragment() {

    private lateinit var tripsBinding: FragmentGuideTripsBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        tripsBinding = FragmentGuideTripsBinding.bind(inflater.inflate(R.layout.fragment_guide_trips, container, false))

        initViews()

        return tripsBinding.root
    }

    private fun initViews() {
        tripsBinding.rvGuidesTrips.layoutManager = LinearLayoutManager(requireContext())



    }
}