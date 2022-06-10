package com.caravan.caravan.ui.activity

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.findNavController
import com.caravan.caravan.R


class GuideOptionActivity : BaseActivity() {
    lateinit var fragInstance: Fragment
    private lateinit var navController: NavController
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_guide_option)
        navController = findNavController(R.id.nav_fragment)
        val isGuide = intent.getBooleanExtra("isGuide", false)
        val isTrip = intent.getBooleanExtra("isTrip", false)
        if (isTrip) navController.navigate(R.id.action_turistGuideOptionFragment_to_createTrip1Fragment)
        if (isGuide) navController.navigate(R.id.action_turistGuideOptionFragment_to_guideGuideOptionFragment)
    }

}