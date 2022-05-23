package com.caravan.caravan.model.auth

import com.caravan.caravan.model.Device
import com.caravan.caravan.model.Language

data class LoginSend(
    val phoneNumber: String,
    val smsCode: Int = 0,
    val device: Device? = null,
    val appLanguage: String = "en" // en, ru, uz
)
