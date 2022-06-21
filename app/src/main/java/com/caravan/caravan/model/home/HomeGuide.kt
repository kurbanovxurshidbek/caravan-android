package com.caravan.caravan.model.home

import com.caravan.caravan.model.*

data class HomeGuide(
    val id: String,
    val name: String,
    val surname: String,
    val profilePhoto: String,
    val rate: Double,
    val price: Price,
    val travelLocations: ArrayList<Location>,
)
