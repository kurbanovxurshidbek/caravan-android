package com.caravan.caravan.model

data class Price(
    val price: Double,
    val currency: String = "UZS", // UZS, USD, RUB
    val option: String // Hour, Day, Trip, Person
)
