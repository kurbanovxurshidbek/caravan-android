package com.caravan.caravan.model

import java.io.Serializable

data class Profile(
    val id: String,
    var name: String,
    var surname: String,
    var phoneNumber: String,
    var email: String? = null,
    var role: String = "TOURIST", // GUIDE
    var guideId: String? = null,
    var status: String = "ACTIVE", // BLOCK
    var photo: String? = null,
    var gender: String, // MALE, FEMALE
    var birthDate: String? = null,
    val createdDate: String,
    val updatedDate: String? = null,
    var appLanguage: String, // en, ru, uz
    val devices: ArrayList<Device>,
    val token: String = ""
) : Serializable