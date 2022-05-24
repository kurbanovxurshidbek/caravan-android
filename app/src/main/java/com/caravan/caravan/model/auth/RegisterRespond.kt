package com.caravan.caravan.model.auth

import com.caravan.caravan.model.Profile

data class RegisterRespond(
    val isRegistered: Boolean = true,
    val title: String? = null,
    val message: String? = null,
    val profile: Profile?
)
