package com.caravan.caravan.model

data class Trip(
    val id: String,
    val name: String,
    var photos: ArrayList<TourPhoto>,
    val facilities: ArrayList<Facility>,
    val locations: ArrayList<Location>,
    val description: String,
    val price: Price,
    val minPeople: Int,
    val maxPeople: Int,
    val guide: GuideProfile,
    val phoneNumber: String,
    val rate: Double,
    val days: Int,
    val attendancesProfileId: ArrayList<String>?,
    val reviews: ArrayList<Comment>? = null ,
    val reviewsCount:Int
)

data class Facility(
    val id: String? = null,
    val title: String,
    val description: String
)

data class CreateTrip(
    val photoId:String,
    val photo:String,
    val location:Location
)

data class TourPhoto(
    val id: String? = null,
    val location: Location,
    val photo: String
)

data class TripRes(
    val currentPageItems:Int,
    val currentPageNumber:Int,
    val totalItems:Int,
    val totalPage:Int,
    val trips:ArrayList<Trip>
)
