package com.caravan.caravan.ui.activity

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import com.caravan.caravan.R
import com.caravan.caravan.manager.SharedPref
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.messaging.FirebaseMessaging
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
        loadFCMToken()
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

    private fun loadFCMToken() {
        FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
            if (!task.isSuccessful) {
                Log.d("MainActivity", "Fetching FCM registration token failed")
                return@OnCompleteListener
            }
            // Get new FCM registration token
            // Save it in locally to use later
            val token = task.result
            Log.d("DeviceToken", token.toString())

            SharedPref(this).saveString("deviceToken", token)

        })
    }

}