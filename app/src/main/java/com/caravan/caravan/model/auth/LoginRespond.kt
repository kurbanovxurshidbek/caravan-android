package com.caravan.caravan.model.auth

import com.caravan.caravan.model.GuideProfile
import com.caravan.caravan.model.Profile

data class LoginRespond(
    val title: String?,
    val message: String?,
    val isExist: Boolean = false,
    val isGuide: Boolean = false,
    val profile: Profile? = null,
    val guideProfile: GuideProfile? = null
)
