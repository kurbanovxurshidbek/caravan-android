package com.caravan.caravan.ui.activity

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.provider.Settings
import androidx.appcompat.app.AppCompatActivity
import com.caravan.caravan.R
import com.caravan.caravan.manager.SharedPref
import com.caravan.caravan.model.*
import com.caravan.caravan.utils.Dialog
import com.caravan.caravan.utils.OkInterface

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
        return Device(deviceId, deviceToken, deviceType)
    }

    open fun showNoConnectionDialog() {
        Dialog.showDialogWarning(this, getString(R.string.str_no_connection), getString(
            R.string.str_try_again), object: OkInterface {
            override fun onClick() {
                return
            }
        })
    }

    open fun showLoading() {
        Dialog.showLoading(this)
    }

    open fun dismissLoading() {
        Dialog.dismissLoading()
    }

}