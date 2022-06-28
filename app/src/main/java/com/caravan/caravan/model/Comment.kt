package com.caravan.caravan.model

import java.io.Serializable

data class Comment(
    val id: String,
    val rate: Int,
    val reviewTime: String,
    val reviewContent: String,
    val from: CommentProfile,
    val commentType: String = "TRIP", // GUIDE
    val trip: Trip? = null,
    val guide: CommentProfile?,
    val answerTime: String? = null,
    val answerContent: String? = null
) : Serializable

data class CommentProfile(
    val id: String,
    val name: String,
    val surname: String,
    val photo: String
)