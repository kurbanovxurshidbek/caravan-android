package com.caravan.caravan.model

data class Device (
    val deviceId: String,
    val deviceToken: String,
    val deviceType: Char = 'A'
)
