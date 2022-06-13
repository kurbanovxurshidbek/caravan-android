package com.caravan.caravan.model.create_trip

import com.caravan.caravan.model.Location
import com.caravan.caravan.model.Price

data class FirstSend(
    val id:String,
    val name: String,
    val travelLocations: ArrayList<Location>,
    val minPeople: Int,
    val maxPeople: Int,
    val desc: String,
    val days:Int
)
