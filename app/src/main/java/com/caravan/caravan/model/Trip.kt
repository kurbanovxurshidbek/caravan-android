package com.caravan.caravan.model

import com.caravan.caravan.model.home.HomeTrip
import com.caravan.caravan.model.search.SearchGuide

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
    val guide: SearchGuide,
    val phoneNumber: String,
    val rate: Double,
    val days: Int,
    val attendances: ArrayList<ProfileId>?,
    val reviewsCount: Int = 0,
    val isComment: Boolean
)

data class ProfileId (
    val profileId: String
)

data class Facility(
    val id: String? = null,
    val title: String,
    val description: String
)

data class CreateTrip(
    val photoId: String,
    val photo: String,
    val location: Location
)

data class TourPhoto(
    val id: String? = null,
    val location: Location,
    val photo: String
)

data class TripRes(
    val currentPageItems: Int,
    val currentPageNumber: Int,
    val totalItems: Int,
    val totalPage: Int,
    val trips: ArrayList<HomeTrip>
)
