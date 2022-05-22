package com.caravan.caravan.model.auth

import com.caravan.caravan.model.Device

data class RegisterSend (
    val name: String,
    val surname: String,
    val phoneNumber: String,
    val gender: String,
)