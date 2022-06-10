package com.caravan.caravan.ui.activity

import android.os.Bundle
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.caravan.caravan.R
import com.caravan.caravan.databinding.ActivityMainBinding
import com.caravan.caravan.utils.Extensions.toast
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : BaseActivity() {
    lateinit var mainBinding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(mainBinding.root)

        //Initialize the bottom navigation view
        //create bottom navigation view object
        val navController = findNavController(R.id.nav_fragment)
        mainBinding.bottomNavigationView.setupWithNavController(navController)


    }
}