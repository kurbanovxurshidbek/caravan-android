package com.caravan.caravan.model

data class Price(
    val cost: Long,
    val currency: String = "UZS", // UZS, USD, RUB
    val type: String // HOUR, DAY, TRIP, PERSON
)
