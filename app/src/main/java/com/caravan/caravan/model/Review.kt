package com.caravan.caravan.model

import java.io.Serializable

data class Review(
    val rate: Int,
    val reviewContent: String,
    val from: String,
    val commentType: String = "TRIP", // GUIDE
    val tripId: String? = null,
    val guideId: String?,
): Serializable
