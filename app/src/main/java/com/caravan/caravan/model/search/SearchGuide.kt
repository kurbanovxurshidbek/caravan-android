package com.caravan.caravan.model.search

import com.caravan.caravan.model.Location
import com.caravan.caravan.model.Price

data class SearchGuide(
    val id: String,
    val name: String,
    val surname: String,
    val profilePhoto: String,
    val rate: Double,
    val price: Price,
    val locations: ArrayList<Location>,
    val travelLocations: ArrayList<Location>,
    val reviewCount: Int
)
