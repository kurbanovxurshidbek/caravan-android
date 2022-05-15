package com.caravan.caravan.model.login

data class LoginSend(
    val phoneNumber: String,
    val phoneID: String,
    val phoneToken: String,
    val phoneType: Char = 'A',
    val appLanguage: String
)
