package com.caravan.caravan.model.create_trip

import com.caravan.caravan.model.Facility
import com.caravan.caravan.model.Price

data class SecondSend(
    val photos: ArrayList<String>,
    val facilities: ArrayList<Facility>,
    val price: Price
)
