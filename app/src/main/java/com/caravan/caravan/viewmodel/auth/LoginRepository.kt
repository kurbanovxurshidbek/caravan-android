package com.caravan.caravan.viewmodel.auth

import com.caravan.caravan.model.auth.LoginSend
import com.caravan.caravan.network.ApiService

class LoginRepository(private val apiService: ApiService) {

    suspend fun sendSMS(loginSend: LoginSend) = apiService.sendSmsCode(loginSend)

    suspend fun checkSMS(loginSend: LoginSend) = apiService.checkSmsCode(loginSend)

}