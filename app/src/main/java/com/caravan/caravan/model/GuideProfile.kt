package com.caravan.caravan.model

data class GuideProfile(
    val id: Long,
    val profile: Profile,
    val phoneNumber: String? = null,
    val biography: String,
    val isHiring: Boolean = false,
    val rate: Double,
    val price: Price,
    val languages: ArrayList<Language>,
    val travelLocations: ArrayList<Location>,
    val comments: ArrayList<Comment>,
    val trips: ArrayList<Trip>,
    val socialNetworks: ArrayList<SocialNetwork>
)

data class SocialNetwork (
    val id: Long,
    val type: String, // Instagram, Facebook, WhatsApp, Telegram
    val link: String,
)

data class Language (
    val id: Long,
    val language: String,
    val level: String
)
