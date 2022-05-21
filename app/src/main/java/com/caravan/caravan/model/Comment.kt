package com.caravan.caravan.model

data class Comment(
    val id: Long,
    val rate: Int,
    val info: String,
    val from: Profile,
    val isForTrip: Boolean = false,
    val trip: Trip? = null,
    val isAnswered: Boolean = false,
    val answer: String? = null
)
