package com.caravan.caravan.utils

import android.app.Activity
import android.widget.Toast
import androidx.fragment.app.Fragment

object Extensions {

    fun Activity.toast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    fun Fragment.toast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }

}