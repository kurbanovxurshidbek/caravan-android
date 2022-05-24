package com.caravan.caravan.ui.activity

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Build
import android.provider.Settings
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.caravan.caravan.manager.SharedPref
import com.caravan.caravan.model.*
import java.time.LocalDateTime

open class BaseActivity : AppCompatActivity() {

    open fun callMainActivity() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }

    open fun callLoginActivity() {
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
        finish()
    }

    fun callIntroActivity() {
        val intent = Intent(this, IntroActivity::class.java)
        startActivity(intent)
        finish()
    }

    open fun getDeviceInfo(context: Context): Device? {
        val deviceId = Settings.Secure.getString(contentResolver, Settings.Secure.ANDROID_ID)
        val deviceToken = SharedPref(context).getString("deviceToken") ?: ""
        val deviceType = 'A'
        var device = Device(deviceId, deviceToken, deviceType)
        return device
    }

}