package com.caravan.caravan.ui.activity

import android.os.Bundle
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

        if (!intent.getStringExtra("guideId").isNullOrBlank()) {
            val guideId = intent.getStringExtra("guideId")
            val bundle = Bundle()
            bundle.putString("guideId", guideId)
            bundle.putBoolean("fromAnotherActivity", true)

            navController = findNavController(R.id.details_nav_fragment)
            val graph = navController.navInflater.inflate(R.navigation.detail_navigation)
            graph.setStartDestination(R.id.guideDetailsFragment)
            navController.setGraph(graph, bundle)

        } else {
            val tripId = intent.getStringExtra("tripId")
            val bundle = Bundle()
            bundle.putString("tripId", tripId)
            navController = findNavController(R.id.details_nav_fragment)
            navController.setGraph(R.navigation.detail_navigation, bundle)
        }

        initViews()
    }

    private fun initViews() {

    }
}