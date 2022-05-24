package com.caravan.caravan.model

import java.time.LocalDateTime

data class Comment(
    val id: Long,
    val rate: Int,
    val reviewTime: LocalDateTime,
    val info: String,
    val from: Profile,
    val isForTrip: Boolean = false,
    val trip: Trip? = null,
    val isAnswered: Boolean = false,
    val answerTime: LocalDateTime,
    val answer: String? = null
)
