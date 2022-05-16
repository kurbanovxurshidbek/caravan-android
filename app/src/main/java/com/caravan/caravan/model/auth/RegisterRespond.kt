package com.caravan.caravan.model.auth

import com.caravan.caravan.model.Profile

data class RegisterRespond(
    val isRegistered: Boolean,
    val message: String,
    val profile: Profile? = null
)
