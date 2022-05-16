package com.caravan.caravan.model

import java.time.LocalDateTime

data class Tour (
    val id: Long,
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
    val comments: ArrayList<Comment>
)

data class Facility (
    val id: Long,
    val title: String,
    val description: String
)

data class TourPhoto (
    val id: Long,
    val size: Long,
    val extension: String,
    val location: Location,
    val createdDate: LocalDateTime,
    val updatedDate: LocalDateTime? = null,
    val url: String
)
