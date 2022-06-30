package com.caravan.caravan.model

import com.caravan.caravan.model.home.HomeTrip
import java.io.Serializable

data class Comment(
    val id: String,
    val rate: Int,
    val reviewTime: String,
    val reviewContent: String,
    val from: CommentProfile,
    val commentType: String = "TRIP", // GUIDE
    val trip: HomeTrip? = null,
    val guide: CommentProfile? = null,
    val answerTime: String? = null,
    val answerContent: String? = null
) : Serializable

data class CommentProfile(
    val id: String,
    val name: String,
    val surname: String,
    val photo: String? = null
)