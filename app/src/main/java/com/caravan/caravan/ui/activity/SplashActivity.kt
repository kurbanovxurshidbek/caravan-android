package com.caravan.caravan.ui.activity

import android.annotation.SuppressLint
import android.os.Bundle
import com.caravan.caravan.R

@SuppressLint("CustomSplashScreen")
class SplashActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
    }
}