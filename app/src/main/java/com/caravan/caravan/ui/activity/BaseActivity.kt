package com.caravan.caravan.ui.activity

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.provider.Settings
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import com.caravan.caravan.R
import com.caravan.caravan.databinding.AlertDialogBinding
import com.caravan.caravan.databinding.DialogLoadingBinding
import com.caravan.caravan.databinding.DialogMessageBinding
import com.caravan.caravan.databinding.DialogWarningBinding
import com.caravan.caravan.manager.SharedPref
import com.caravan.caravan.model.Device
import com.caravan.caravan.utils.*
import com.caravan.caravan.utils.Extensions.setTransparentWindow

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
        showDialogWarning(getString(R.string.str_no_connection), getString(
            R.string.str_try_again
        ), object : OkInterface {
            override fun onClick() {
                return
            }
        })
    }

    open fun showAlertDialog(title: String, action: OkWithCancelInterface) {
        val dialog = DialogAlert(title)
        dialog.noListener = {
            action.onCancelClick()
            dialog.dismiss()
        }
        dialog.yesListener = {
            action.onOkClick()
            dialog.dismiss()
        }
        dialog.show(supportFragmentManager, "alert_dialog")
    }

    fun showDialogMessage(title: String, message: String, ok: OkInterface) {
        val dialog = DialogMessage(title, message)
        dialog.okListener = {
            ok.onClick()
            dialog.dismiss()
        }
        dialog.show(supportFragmentManager, "message_dialog")
    }

    fun showDialogWarning(title: String, message: String, ok: OkInterface) {
        val dialog = DialogWarning(title, message)
        dialog.okListener = {
            ok.onClick()
            dialog.dismiss()
        }
        dialog.show(supportFragmentManager, "warning_dialog")
    }

    private var loadingDialog: android.app.Dialog? = null

    open fun showLoading() {
        if (loadingDialog == null) {
            loadingDialog = android.app.Dialog(this)
            val loadingBinding = DialogLoadingBinding.inflate(LayoutInflater.from(this))
            loadingDialog?.setContentView(loadingBinding.root)
            loadingDialog?.setCancelable(false)

            loadingDialog?.setTransparentWindow()
            loadingDialog?.window?.setLayout(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
            loadingDialog?.show()
        }

    }

    open fun dismissLoading() {
        loadingDialog?.dismiss()
        loadingDialog = null
    }

    override fun onDestroy() {
        super.onDestroy()
    }
}