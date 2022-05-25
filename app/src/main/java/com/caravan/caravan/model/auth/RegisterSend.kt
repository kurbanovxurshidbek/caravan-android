package com.caravan.caravan.model.auth

data class RegisterSend(
    val name: String,
    val surname: String,
    val phoneNumber: String,
    val gender: String,
)