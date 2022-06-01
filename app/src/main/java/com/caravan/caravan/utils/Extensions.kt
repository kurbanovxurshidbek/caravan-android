package com.caravan.caravan.utils

import android.app.Activity
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import android.app.Dialog
import androidx.fragment.app.Fragment

object Extensions {

    private lateinit var launcher: ActivityResultLauncher<Array<String>>

    fun Activity.toast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    fun Fragment.toast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }

    fun Dialog.setTransparentWindow() {
        window?.setBackgroundDrawableResource(android.R.color.transparent)
    }

}