package com.caravan.caravan.ui.activity

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.CountDownTimer
import android.view.WindowManager
import com.caravan.caravan.R
import com.caravan.caravan.manager.SharedPref
import java.util.*

@SuppressLint("CustomSplashScreen")
class SplashActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        initViews()
    }

    private fun initViews() {
        saveDeviceLanguage()
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

    private fun checkSaved() {
        if (SharedPref(this).getBoolean("introDone")) {
            if (SharedPref(this).getBoolean("loginDone")) callMainActivity()
            else {
                callLoginActivity()
            }
        } else {
            callIntroActivity()
        }
    }


    private fun saveDeviceLanguage() {
        val appLanguage: String = Locale.getDefault().language
        SharedPref(this).saveString("appLanguage", appLanguage)
    }
}