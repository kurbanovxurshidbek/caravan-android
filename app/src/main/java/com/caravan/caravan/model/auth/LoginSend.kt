package com.caravan.caravan.model.auth

import com.caravan.caravan.model.Device

data class LoginSend(
    val phoneNumber: String,
    val smsCode: Int? = null,
    val device: Device? = null
)
