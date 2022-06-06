package com.caravan.caravan.model.upgrade

import com.caravan.caravan.model.*

data class UpgradeSend (
    val profileId: String,
    val secondPhoneNumber: String? = null,
    val biography: String,
    val price: Price,
    val languages: ArrayList<Language>,
    val travelLocations: ArrayList<Location>
)