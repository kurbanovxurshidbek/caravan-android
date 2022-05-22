package com.caravan.caravan.ui.fragment.main

import android.os.Bundle
import android.view.View
import com.caravan.caravan.R
import com.caravan.caravan.databinding.FragmentHomeBinding
import com.caravan.caravan.ui.fragment.BaseFragment
import com.caravan.caravan.utils.viewBinding

class HomeFragment : BaseFragment(R.layout.fragment_home) {

    private val binding by viewBinding { FragmentHomeBinding.bind(it) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)



    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)



    }

}