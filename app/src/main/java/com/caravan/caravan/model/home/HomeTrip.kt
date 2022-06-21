package com.caravan.caravan.model.home

import com.caravan.caravan.model.Price

data class HomeTrip(
    val id: String,
    val name: String,
    val photo: String,
    val price: Price,
    val rate: Double,
    val reviewsCount: Int
)
