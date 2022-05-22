package com.caravan.caravan.model

import java.io.Serializable
import java.time.LocalDate
import java.time.LocalDateTime

data class Profile(
    val id: Long,
    val name: String,
    val surname: String,
    val phoneNumber: String,
    val email: String? = null,
    val role: String = "TOURIST", // GUIDE
    val status: String = "ACTIVE", // BLOCK
    val profilePhoto: String? = null,
    val gender: String, // MALE, FEMALE
    val birthDate: LocalDate? = null,
    val createdDate: LocalDateTime,
    val updatedDate: LocalDateTime? = null,
    val appLanguage: String, // en, ru, uz
    val devices: ArrayList<Device>
):Serializable