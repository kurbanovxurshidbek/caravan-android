package com.caravan.caravan.ui.activity

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.os.CountDownTimer
import android.view.WindowInsetsController
import android.view.WindowManager
import androidx.annotation.RequiresApi
import com.caravan.caravan.R
import com.caravan.caravan.manager.SharedPref

@SuppressLint("CustomSplashScreen")
class SplashActivity : BaseActivity() {
    @RequiresApi(Build.VERSION_CODES.R)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        window.decorView.windowInsetsController?.setSystemBarsAppearance(WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS, WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS)
        initViews()
    }

    private fun initViews() {
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        countDownTimer()

    }

    private fun countDownTimer() {
        object : CountDownTimer(2000, 1000) {
            override fun onTick(l: Long) {}
            override fun onFinish() {
                checkSaved()
            }
        }.start()
    }

    private fun checkSaved(){
        if (SharedPref(this).getBoolean("introDone")){
            callMainActivity()
        }else{
            callIntroActivity()
        }
    }
}