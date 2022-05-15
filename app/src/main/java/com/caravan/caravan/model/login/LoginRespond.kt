package com.caravan.caravan.model.login

import com.caravan.caravan.model.Profile

data class LoginRespond(
    val isHave: Boolean = false,
    val profile: Profile? = null
)
