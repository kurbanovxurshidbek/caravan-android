package com.caravan.caravan.model.home

data class HomeRespond(
    val status: Boolean = true,
    val title: String? = null,
    val message: String? = null,
    val topGuides: ArrayList<HomeGuide>,
    val topTrips: ArrayList<HomeTrip>
)