package com.caravan.caravan.model.search

import com.caravan.caravan.model.Price

data class SearchTripSend(
    val key: String,
    val filterTrip: FilterTrip
)

data class FilterTrip(
    val minPrice: Price,
    val maxPrice: Price,
    val minRating: Int = 1,
    val maxRating: Int = 5,
    val day: Int,
    val minPeople: Int,
    val maxPeople: Int
)