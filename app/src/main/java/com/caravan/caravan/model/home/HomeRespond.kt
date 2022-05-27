package com.caravan.caravan.model.home

import com.caravan.caravan.model.GuideProfile
import com.caravan.caravan.model.Trip

data class HomeRespond(
    val status: Boolean = true,
    val title: String? = null,
    val message: String? = null,
    val topGuides: ArrayList<GuideProfile>,
    val topTrips: ArrayList<Trip>
)