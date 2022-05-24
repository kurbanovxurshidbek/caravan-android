package com.caravan.caravan.ui.activity

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.CountDownTimer
import android.view.WindowManager
import androidx.annotation.RequiresApi
import com.caravan.caravan.R
import com.caravan.caravan.manager.SharedPref
import java.util.*

@SuppressLint("CustomSplashScreen")
class SplashActivity : BaseActivity() {
    @RequiresApi(Build.VERSION_CODES.R)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        initViews()
    }

    private fun initViews() {
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
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