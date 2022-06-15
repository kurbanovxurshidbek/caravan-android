package com.caravan.caravan.model.create_trip

import com.caravan.caravan.model.Location

data class TripUploadPhoto(
    val id:String,
    val photoId: String,
    val location: Location
)
