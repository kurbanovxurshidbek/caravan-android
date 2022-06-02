package com.caravan.caravan.model

import java.io.Serializable

data class GuideProfile(
    val id: String,
    val profile: Profile,
    val phoneNumber: String? = null,
    val biography: String,
    val isHiring: Boolean = false,
    val rate: Double,
    val price: Price,
    val languages: ArrayList<Language>,
    val travelLocations: ArrayList<Location>,
    val reviews: ArrayList<Comment>?,
    val attendancesProfileId: ArrayList<String>,
    val trips: ArrayList<Trip>?
) : Serializable

data class Language(
    val id: String? = null,
    val name: String,
    val level: String
)
