package com.caravan.caravan.utils

import android.content.Context
import android.content.pm.PackageManager

import androidx.core.app.ActivityCompat


object RequestPermissions {

    private var permissions: ArrayList<String>?=null




    fun hasPermissions(context: Context?, permissions: ArrayList<String>?): Boolean {
        if (context != null && permissions != null) {
            permissions.forEach { permission ->
                if (ActivityCompat.checkSelfPermission(context, permission) !=
                    PackageManager.PERMISSION_GRANTED) {
                    return false
                }
            }
        }
        return true
    }
}