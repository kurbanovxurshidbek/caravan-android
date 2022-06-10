package com.caravan.caravan.model

import com.caravan.caravan.model.GuideProfile
import com.caravan.caravan.model.Profile
import com.caravan.caravan.model.Trip
import java.io.Serializable

data class Comment(
    val id: String,
    val rate: Int,
    val reviewTime: String,
    val reviewContent: String,
    val from: Profile,
    val commentType: String = "TRIP", // GUIDE
    val trip: Trip? = null,
    val guide: GuideProfile?,
    val answerTime: String? = null,
    val answerContent: String? = null
): Serializable
