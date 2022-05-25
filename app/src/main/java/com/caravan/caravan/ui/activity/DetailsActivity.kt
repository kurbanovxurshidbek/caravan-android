package com.caravan.caravan.ui.activity

import android.os.Bundle
import androidx.core.os.bundleOf
import androidx.navigation.NavController
import androidx.navigation.findNavController
import com.caravan.caravan.R
import com.caravan.caravan.databinding.ActivityDetailsBinding

class DetailsActivity : BaseActivity() {
    lateinit var detailsBinding: ActivityDetailsBinding
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        detailsBinding = ActivityDetailsBinding.inflate(layoutInflater)
        setContentView(detailsBinding.root)

        val tripId = intent.getStringExtra("tripId")
        val bundle = Bundle()
        bundle.putString("tripId", tripId)
        navController = findNavController(R.id.details_nav_fragment)
        navController.setGraph(R.navigation.detail_navigation, bundle)
        initViews()
    }

    private fun initViews() {

    }
}