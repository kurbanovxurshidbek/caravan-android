package com.caravan.caravan.viewmodel.auth

import android.util.Log
import com.caravan.caravan.model.auth.LoginSend
import com.caravan.caravan.model.auth.RegisterRespond
import com.caravan.caravan.model.auth.RegisterSend
import com.caravan.caravan.network.ApiService
import retrofit2.Response

class LoginRepository(private val apiService: ApiService) {

    suspend fun sendSMS(loginSend: LoginSend) = apiService.sendSmsCode(loginSend)

    suspend fun checkSMS(loginSend: LoginSend) = apiService.checkSmsCode(loginSend)

    suspend fun register(registerSend: RegisterSend): Response<RegisterRespond> {
        Log.d("TAG", "register: ${apiService.registerUser(registerSend)}")
        return  apiService.registerUser(registerSend)
    }

}