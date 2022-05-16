package com.caravan.caravan.model

data class Device (
    val id: Long,
    val deviceId: String,
    val deviceToken: String,
    val deviceType: Char = 'A'
)
