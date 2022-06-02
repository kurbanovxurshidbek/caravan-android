package com.caravan.caravan.model.create_trip

import com.caravan.caravan.model.Location

data class UploadImageForTrip(
    val photoId: String,
    val location: Location
)
