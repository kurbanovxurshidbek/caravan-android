package com.caravan.caravan.model.review

import java.io.Serializable

data class Review(
    val rate: Int,
    val content: String,
    val type: String = "TRIP", // GUIDE
    val tripId: String? = null,
    val guideId: String?,
): Serializable
