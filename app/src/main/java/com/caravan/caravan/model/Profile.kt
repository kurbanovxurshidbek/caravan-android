package com.caravan.caravan.model

import java.time.LocalDate
import java.time.LocalDateTime

data class Profile(
    val id: Long,
    val name: String,
    val surname: String,
    val phoneNumber: String,
    val email: String? = null,
    val role: String = "TOURIST",
    val status: String = "ACTIVE",
    val profilePhoto: String? = null,
    val gender: String,
    val birthDate: LocalDate? = null,
    val createdDate: LocalDateTime,
    val updatedDate: LocalDateTime? = null,
    val appLanguage: String,
    val devices: ArrayList<Device>
)