package com.caravan.caravan.model.home

import com.caravan.caravan.model.Price
import com.caravan.caravan.model.TourPhoto
import com.caravan.caravan.model.create_trip.PhotoRespond

data class HomeTrip(
    val id: String,
    val name: String,
    val photo: String,
    val price: Price,
    val rate: Double,
    val reviewsCount: Int,
    val photos: ArrayList<TourPhoto>
)
