package com.caravan.caravan.model

data class Trip(
    val id: String,
    val name: String,
    val photos: ArrayList<TourPhoto>,
    val facility: ArrayList<Facility>,
    val places: ArrayList<Location>,
    val description: String,
    val price: Price,
    val minPeople: Int,
    val maxPeople: Int,
    val guideProfile: GuideProfile,
    val phoneNumber: String,
    val rate: Double,
    val attendancesProfileId: ArrayList<String>,
    val reviews: ArrayList<Comment>? = null
)

data class Facility(
    val id: String? = null,
    val title: String,
    val description: String
)
data class CreateTrip(
    val photo:String,
    val location:Location
)

data class TourPhoto(
    val id: String? = null,
    val location: Location,
    val url: String
)
